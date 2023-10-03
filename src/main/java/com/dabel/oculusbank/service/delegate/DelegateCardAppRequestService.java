package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.AccountChecker;
import com.dabel.oculusbank.app.Fee;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Fees;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardAppRequestDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CardAppRequestService;
import com.dabel.oculusbank.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DelegateCardAppRequestService implements OperationAcknowledgment<CardAppRequestDTO> {

    @Autowired
    CardAppRequestService cardAppRequestService;
    @Autowired
    AccountService accountService;
    @Autowired
    FeeService feeService;

    public CardAppRequestDTO sendRequest(CardAppRequestDTO cardAppRequestDTO) {

        TrunkDTO account = accountService.findTrunkByNumberAndCustomerIdentity(cardAppRequestDTO.getAccountNumber(), cardAppRequestDTO.getCustomerIdentityNumber());

        if(!AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible for this operation");

        if(account.getBalance() < Fees.CARD_APP_REQUEST) {

            CardAppRequestDTO failedCardApp = CardAppRequestDTO.builder()
                    .accountId(account.getAccountId())
                    .customerId(account.getCustomerId())
                    .cardType(cardAppRequestDTO.getCardType())
                    .status(Status.Failed.code())
                    .failure_reason("Account balance is insufficient for card application request fees")
                    .build();

            cardAppRequestService.save(failedCardApp);
            throw new BalanceInsufficientException("Account balance is insufficient for application fees");
        }

        CardAppRequestDTO successCardApp = CardAppRequestDTO.builder()
                .accountId(account.getAccountId())
                .cardType(cardAppRequestDTO.getCardType())
                .status(Status.Pending.code())
                .build();

        return cardAppRequestService.save(successCardApp);
    }

    @Override
    public CardAppRequestDTO approve(int operationId) {

        CardAppRequestDTO cardApp = cardAppRequestService.findById(operationId);
        cardApp.setStatus(Status.Approved.code());
        cardApp.setUpdatedAt(LocalDateTime.now());

        AccountDTO account = accountService.findByNumber(cardApp.getAccountNumber());
        feeService.apply(account, new Fee(Fees.CARD_APP_REQUEST, "Card application request"));

        return cardAppRequestService.save(cardApp);
    }

    @Override
    public CardAppRequestDTO reject(int operationId, String remarks) {
        CardAppRequestDTO cardApp = cardAppRequestService.findById(operationId);
        cardApp.setStatus(Status.Rejected.code());
        cardApp.setFailure_reason(remarks);
        cardApp.setUpdatedAt(LocalDateTime.now());

        return cardAppRequestService.save(cardApp);
    }

    public List<CardAppRequestDTO> findAll() {
        return cardAppRequestService.findAll();
    }

    public CardAppRequestDTO findById(int requestId) {return cardAppRequestService.findById(requestId);}
}
