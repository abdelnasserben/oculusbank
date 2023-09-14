package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CardAppRequestDTO;
import com.dabel.oculusbank.exception.CardAppNotFoundException;
import com.dabel.oculusbank.mapper.CardAppRequestMapper;
import com.dabel.oculusbank.model.CardAppRequest;
import com.dabel.oculusbank.model.CardAppRequestView;
import com.dabel.oculusbank.repository.CardAppRequestRepository;
import com.dabel.oculusbank.repository.CardAppRequestViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardAppRequestService {

    @Autowired
    CardAppRequestRepository cardAppRequestRepository;
    @Autowired
    CardAppRequestViewRepository cardAppRequestViewRepository;

    public CardAppRequestDTO save(CardAppRequestDTO cardAppRequestDTO) {
        CardAppRequest card = cardAppRequestRepository.save(CardAppRequestMapper.toEntity(cardAppRequestDTO));
        return CardAppRequestMapper.toDTO(card);
    }

    public CardAppRequestDTO findById(int requestId) {
        CardAppRequestView cardView = cardAppRequestViewRepository.findById(requestId)
                .orElseThrow(CardAppNotFoundException::new);
        return formatStatusToNameAndGetDTO(cardView);
    }


    private static CardAppRequestDTO formatStatusToNameAndGetDTO(CardAppRequestView cardAppRequestView) {
        cardAppRequestView.setStatus(Status.nameOf(cardAppRequestView.getStatus()));
        return CardAppRequestMapper.viewToDTO(cardAppRequestView);
    }
}
