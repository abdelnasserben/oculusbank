package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.AccountChecker;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DelegateCardService {

    @Autowired
    CardService cardService;
    @Autowired
    AccountService accountService;

    public CardDTO add(CardDTO cardDTO) {

        AccountDTO account = accountService.findTrunkByNumber(cardDTO.getAccountNumber());

        if(!AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible to receive a card");

        //TODO: save card
        cardDTO.setAccountId(account.getAccountId());
        cardDTO.setStatus(Status.Pending.code());
        return cardService.save(cardDTO);
    }

    public CardDTO activate(int operationId) {

        CardDTO card = cardService.findById(operationId);

        if(card.getStatus().equals(Status.Active.name()))
            throw new IllegalOperationException("Card already active");

        card.setStatus(Status.Active.code());
        card.setUpdatedBy("Administrator");
        card.setUpdatedAt(LocalDateTime.now());

        return cardService.save(card);
    }

    public CardDTO deactivate(int operationId, String remarks) {

        CardDTO card = cardService.findById(operationId);

        if(!card.getStatus().equals(Status.Active.name()))
            throw new IllegalOperationException("Cannot deactivate this card");

        card.setStatus(Status.Deactivated.code());
        card.setFailureReason(remarks);
        card.setUpdatedBy("Administrator");
        card.setUpdatedAt(LocalDateTime.now());
        return cardService.save(card);
    }

    public List<CardDTO> findAllByCustomerId(int customerId) {

        List<CardDTO> cards = new ArrayList<>();
        List<TrunkDTO> customerAccounts = accountService.findAllTrunksByCustomerId(customerId);

        customerAccounts.stream()
                .map(trunkDTO -> cardService.findAllByAccountId(trunkDTO.getAccountId()))
                .forEach(cards::addAll);

        return cards;
    }

    public CardDTO finById(int cardId) {
        return cardService.findById(cardId);
    }
}
