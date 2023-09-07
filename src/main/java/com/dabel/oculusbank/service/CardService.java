package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.exception.CardNotFoundException;
import com.dabel.oculusbank.mapper.CardMapper;
import com.dabel.oculusbank.model.Card;
import com.dabel.oculusbank.model.CardView;
import com.dabel.oculusbank.repository.CardRepository;
import com.dabel.oculusbank.repository.CardViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardViewRepository cardViewRepository;

    public CardDTO save(CardDTO cardDTO) {
        Card card = cardRepository.save(CardMapper.toEntity(cardDTO));
        return CardMapper.toDTO(card);
    }

    public CardDTO findById(int cardId) {
        CardView cardView = cardViewRepository.findById(cardId)
                .orElseThrow(CardNotFoundException::new);
        return formatStatusToNameAndGetDTO(cardView);
    }

    public CardDTO findByCardNumber(String cardNumber) {
        CardView cardView = cardViewRepository.findByCardNumber(cardNumber)
                .orElseThrow(CardNotFoundException::new);
        return formatStatusToNameAndGetDTO(cardView);
    }

    public List<CardDTO> findAllByAccountId(int accountId) {
        return cardViewRepository.findAllByAccountId(accountId).stream()
                .map(CardService::formatStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    private static CardDTO formatStatusToNameAndGetDTO(CardView cardView) {
        cardView.setStatus(Status.nameOf(cardView.getStatus()));
        return CardMapper.viewToDTO(cardView);
    }
}
