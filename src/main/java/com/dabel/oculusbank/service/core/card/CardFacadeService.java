package com.dabel.oculusbank.service.core.card;

import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.app.util.AccountChecker;
import com.dabel.oculusbank.constant.Fees;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.core.account.TrunkService;
import com.dabel.oculusbank.service.core.fee.FeeTrunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardFacadeService {

    @Autowired
    CardCrudService cardCrudService;
    @Autowired
    CardApplicationCrudService cardApplicationCrudService;
    @Autowired
    TrunkService trunkService;
    @Autowired
    FeeTrunkService feeTrunkService;

    public void sendApplication(CardApplicationDTO cardApplicationDTO) {

        TrunkDTO account = trunkService.findByNumberAndCustomerIdentity(cardApplicationDTO.getAccountNumber(), cardApplicationDTO.getCustomerIdentityNumber());

        if(!AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible for this operation");

        if(account.getBalance() < Fees.CARD_APP_REQUEST) {

            CardApplicationDTO failedCardApp = CardApplicationDTO.builder()
                    .accountId(account.getAccountId())
                    .customerId(account.getCustomerId())
                    .cardType(cardApplicationDTO.getCardType())
                    .status(Status.FAILED.code())
                    .failureReason("Account balance is insufficient for card application request fees")
                    .build();

            cardApplicationCrudService.save(failedCardApp);
            throw new BalanceInsufficientException("Account balance is insufficient for application fees");
        }

        CardApplicationDTO successCardApp = CardApplicationDTO.builder()
                .accountId(account.getAccountId())
                .cardType(cardApplicationDTO.getCardType())
                .status(Status.PENDING.code())
                .build();

        cardApplicationCrudService.save(successCardApp);
    }

    public CardApplicationDTO approve(int operationId) {

        CardApplicationDTO cardApp = cardApplicationCrudService.findById(operationId);
        cardApp.setStatus(Status.APPROVED.code());
        cardApp.setUpdatedAt(LocalDateTime.now());

        AccountDTO account = trunkService.findByNumber(cardApp.getAccountNumber());
        feeTrunkService.apply(account, new Fee(Fees.CARD_APP_REQUEST, "Card application request"));

        return cardApplicationCrudService.save(cardApp);
    }

    public CardApplicationDTO reject(int operationId, String remarks) {
        CardApplicationDTO cardApp = cardApplicationCrudService.findById(operationId);
        cardApp.setStatus(Status.REJECTED.code());
        cardApp.setFailureReason(remarks);
        cardApp.setUpdatedAt(LocalDateTime.now());

        return cardApplicationCrudService.save(cardApp);
    }

    public List<CardApplicationDTO> findAllApplications() {
        return cardApplicationCrudService.findAll();
    }

    public CardApplicationDTO findApplicationById(int requestId) {return cardApplicationCrudService.findById(requestId);}

    public CardDTO add(CardDTO cardDTO) {

        AccountDTO account = trunkService.findByNumber(cardDTO.getAccountNumber());

        if(!AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible to receive a card");

        //TODO: save card
        cardDTO.setAccountId(account.getAccountId());
        cardDTO.setStatus(Status.PENDING.code());
        return cardCrudService.save(cardDTO);
    }

    public void activate(int operationId) {

        CardDTO card = cardCrudService.findById(operationId);

        if(card.getStatus().equals(Status.ACTIVE.code()))
            throw new IllegalOperationException("Card already active");

        card.setStatus(Status.ACTIVE.code());
        card.setUpdatedBy("Administrator");
        card.setUpdatedAt(LocalDateTime.now());

        cardCrudService.save(card);
    }

    public void deactivate(int operationId, String remarks) {

        CardDTO card = cardCrudService.findById(operationId);

        if(!card.getStatus().equals(Status.ACTIVE.code()))
            throw new IllegalOperationException("Cannot deactivate this card");

        card.setStatus(Status.DEACTIVATED.code());
        card.setFailureReason(remarks);
        card.setUpdatedBy("Administrator");
        card.setUpdatedAt(LocalDateTime.now());
        cardCrudService.save(card);
    }

    public List<CardDTO> findAllByCustomerId(int customerId) {

        List<CardDTO> cards = new ArrayList<>();
        List<TrunkDTO> customerAccounts = trunkService.findAllByCustomerId(customerId);

        customerAccounts.stream()
                .map(trunkDTO -> cardCrudService.findAllByAccountId(trunkDTO.getAccountId()))
                .forEach(cards::addAll);

        return cards;
    }

    public CardDTO finById(int cardId) {
        return cardCrudService.findById(cardId);
    }
}
