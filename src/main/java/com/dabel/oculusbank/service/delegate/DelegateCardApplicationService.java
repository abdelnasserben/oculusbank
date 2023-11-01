package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.util.account.AccountChecker;
import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.app.util.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Fees;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CardApplicationService;
import com.dabel.oculusbank.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DelegateCardApplicationService implements OperationAcknowledgment<CardApplicationDTO> {

    @Autowired
    CardApplicationService cardApplicationService;
    @Autowired
    AccountService accountService;
    @Autowired
    FeeService feeService;

    public CardApplicationDTO sendRequest(CardApplicationDTO cardApplicationDTO) {

        TrunkDTO account = accountService.findTrunkByNumberAndCustomerIdentity(cardApplicationDTO.getAccountNumber(), cardApplicationDTO.getCustomerIdentityNumber());

        if(!AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible for this operation");

        if(account.getBalance() < Fees.CARD_APP_REQUEST) {

            CardApplicationDTO failedCardApp = CardApplicationDTO.builder()
                    .accountId(account.getAccountId())
                    .customerId(account.getCustomerId())
                    .cardType(cardApplicationDTO.getCardType())
                    .status(Status.Failed.code())
                    .failureReason("Account balance is insufficient for card application request fees")
                    .build();

            cardApplicationService.save(failedCardApp);
            throw new BalanceInsufficientException("Account balance is insufficient for application fees");
        }

        CardApplicationDTO successCardApp = CardApplicationDTO.builder()
                .accountId(account.getAccountId())
                .cardType(cardApplicationDTO.getCardType())
                .status(Status.Pending.code())
                .build();

        return cardApplicationService.save(successCardApp);
    }

    @Override
    public CardApplicationDTO approve(int operationId) {

        CardApplicationDTO cardApp = cardApplicationService.findById(operationId);
        cardApp.setStatus(Status.Approved.code());
        cardApp.setUpdatedAt(LocalDateTime.now());

        AccountDTO account = accountService.findByNumber(cardApp.getAccountNumber());
        feeService.apply(account, new Fee(Fees.CARD_APP_REQUEST, "Card application request"));

        return cardApplicationService.save(cardApp);
    }

    @Override
    public CardApplicationDTO reject(int operationId, String remarks) {
        CardApplicationDTO cardApp = cardApplicationService.findById(operationId);
        cardApp.setStatus(Status.Rejected.code());
        cardApp.setFailureReason(remarks);
        cardApp.setUpdatedAt(LocalDateTime.now());

        return cardApplicationService.save(cardApp);
    }

    public List<CardApplicationDTO> findAll() {
        return cardApplicationService.findAll();
    }

    public CardApplicationDTO findById(int requestId) {return cardApplicationService.findById(requestId);}
}
