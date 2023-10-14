package com.dabel.oculusbank.service.support.card;

import com.dabel.oculusbank.dto.CardAppRequestDTO;
import com.dabel.oculusbank.exception.CardAppRequestNotFoundException;
import com.dabel.oculusbank.mapper.CardAppRequestMapper;
import com.dabel.oculusbank.model.CardAppRequest;
import com.dabel.oculusbank.model.CardAppRequestView;
import com.dabel.oculusbank.repository.CardAppRequestRepository;
import com.dabel.oculusbank.repository.CardAppRequestViewRepository;
import com.dabel.oculusbank.service.core.card.CardApplicationCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardApplicationCrudServiceImpl implements CardApplicationCrudService {

    @Autowired
    CardAppRequestRepository cardAppRequestRepository;
    @Autowired
    CardAppRequestViewRepository cardAppRequestViewRepository;

    @Override
    public CardAppRequestDTO save(CardAppRequestDTO cardAppRequestDTO) {

        CardAppRequest card = cardAppRequestRepository.save(CardAppRequestMapper.toEntity(cardAppRequestDTO));
        return CardAppRequestMapper.toDTO(card);
    }

    @Override
    public CardAppRequestDTO findById(int requestId) {

        CardAppRequestView cardView = cardAppRequestViewRepository.findById(requestId)
                .orElseThrow(CardAppRequestNotFoundException::new);
        return CardAppRequestMapper.viewToDTO(cardView);
    }

    @Override
    public List<CardAppRequestDTO> findAll() {

        return cardAppRequestViewRepository.findAll().stream()
                .map(CardAppRequestMapper::viewToDTO)
                .toList();
    }

}
