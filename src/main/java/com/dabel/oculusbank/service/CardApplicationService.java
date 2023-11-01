package com.dabel.oculusbank.service;

import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.exception.CardAppRequestNotFoundException;
import com.dabel.oculusbank.mapper.CardApplicationMapper;
import com.dabel.oculusbank.model.CardApplication;
import com.dabel.oculusbank.model.CardApplicationView;
import com.dabel.oculusbank.repository.CardAppApplicationViewRepository;
import com.dabel.oculusbank.repository.CardApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardApplicationService {

    @Autowired
    CardApplicationRepository cardApplicationRepository;
    @Autowired
    CardAppApplicationViewRepository cardAppApplicationViewRepository;

    public CardApplicationDTO save(CardApplicationDTO cardApplicationDTO) {
        CardApplication card = cardApplicationRepository.save(CardApplicationMapper.toEntity(cardApplicationDTO));
        return CardApplicationMapper.toDTO(card);
    }

    public CardApplicationDTO findById(int requestId) {
        CardApplicationView cardView = cardAppApplicationViewRepository.findById(requestId)
                .orElseThrow(CardAppRequestNotFoundException::new);
        return CardApplicationMapper.viewToDTO(cardView);
    }

    public List<CardApplicationDTO> findAll() {
        return cardAppApplicationViewRepository.findAll().stream()
                .map(CardApplicationMapper::viewToDTO)
                .toList();
    }
}
