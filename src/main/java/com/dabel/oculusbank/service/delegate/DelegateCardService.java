package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.AccountChecker;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DelegateCardService implements OperationAcknowledgment<CardDTO> {

    @Autowired
    CardService cardService;
    @Autowired
    AccountService accountService;

    public CardDTO save(CardDTO cardDTO) {

        //TODO: check eligibility of account to receive card
        AccountDTO account = accountService.findByNumber(cardDTO.getAccountNumber());
        if(AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible to receive a card");

        if(accountService.isTrunk(account.getAccountNumber()))
            throw new IllegalOperationException("The account is not eligible to receive a card");

        //TODO: save card
        cardDTO.setStatus(Status.Pending.code());
        return cardService.save(cardDTO);
    }

    @Override
    public CardDTO approve(int operationId) {

        CardDTO card = cardService.findById(operationId);
        card.setStatus(Status.Approved.code());
        card.setUpdatedBy("Administrator");
        card.setUpdatedAt(LocalDateTime.now());

        return cardService.save(card);
    }

    @Override
    public CardDTO reject(int operationId, String remarks) {

        CardDTO card = cardService.findById(operationId);
        card.setStatus(Status.Rejected.code());
        card.setFailureReason(remarks);
        card.setUpdatedBy("Administrator");
        card.setUpdatedAt(LocalDateTime.now());
        return cardService.save(card);
    }
}
