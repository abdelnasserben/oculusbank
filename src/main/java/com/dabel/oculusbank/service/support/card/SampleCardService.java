package com.dabel.oculusbank.service.support.card;

import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.exception.CardNotFoundException;
import com.dabel.oculusbank.mapper.CardMapper;
import com.dabel.oculusbank.model.Card;
import com.dabel.oculusbank.model.CardView;
import com.dabel.oculusbank.repository.CardRepository;
import com.dabel.oculusbank.repository.CardViewRepository;
import com.dabel.oculusbank.service.core.card.CardCruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SampleCardService implements CardCruService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardViewRepository cardViewRepository;

    @Override
    public CardDTO save(CardDTO cardDTO) {

        Card card = cardRepository.save(CardMapper.toEntity(cardDTO));
        return CardMapper.toDTO(card);
    }

    @Override
    public CardDTO findById(int cardId) {

        CardView cardView = cardViewRepository.findById(cardId)
                .orElseThrow(CardNotFoundException::new);
        return CardMapper.viewToDTO(cardView);
    }

    @Override
    public CardDTO findByCardNumber(String cardNumber) {

        CardView cardView = cardViewRepository.findByCardNumber(cardNumber)
                .orElseThrow(CardNotFoundException::new);
        return CardMapper.viewToDTO(cardView);
    }

    @Override
    public List<CardDTO> findAllByAccountId(int accountId) {

        return cardViewRepository.findAllByAccountId(accountId).stream()
                .map(CardMapper::viewToDTO)
                .collect(Collectors.toList());
    }
}
