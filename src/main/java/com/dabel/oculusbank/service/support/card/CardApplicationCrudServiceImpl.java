package com.dabel.oculusbank.service.support.card;

import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.exception.CardAppRequestNotFoundException;
import com.dabel.oculusbank.mapper.CardApplicationMapper;
import com.dabel.oculusbank.model.CardApplication;
import com.dabel.oculusbank.model.CardApplicationView;
import com.dabel.oculusbank.repository.CardAppApplicationViewRepository;
import com.dabel.oculusbank.repository.CardApplicationRepository;
import com.dabel.oculusbank.service.core.card.CardApplicationCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardApplicationCrudServiceImpl implements CardApplicationCrudService {

    @Autowired
    CardApplicationRepository cardApplicationRepository;
    @Autowired
    CardAppApplicationViewRepository cardAppApplicationViewRepository;

    @Override
    public CardApplicationDTO save(CardApplicationDTO cardApplicationDTO) {

        CardApplication card = cardApplicationRepository.save(CardApplicationMapper.toEntity(cardApplicationDTO));
        return CardApplicationMapper.toDTO(card);
    }

    @Override
    public CardApplicationDTO findById(int requestId) {

        CardApplicationView cardView = cardAppApplicationViewRepository.findById(requestId)
                .orElseThrow(CardAppRequestNotFoundException::new);
        return CardApplicationMapper.viewToDTO(cardView);
    }

    @Override
    public List<CardApplicationDTO> findAll() {

        return cardAppApplicationViewRepository.findAll().stream()
                .map(CardApplicationMapper::viewToDTO)
                .toList();
    }

}
