package com.dabel.oculusbank.service.core.card;

import com.dabel.oculusbank.dto.CardApplicationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardApplicationCrudService {

    CardApplicationDTO save(CardApplicationDTO cardApplicationDTO);

    CardApplicationDTO findById(int requestId);

    List<CardApplicationDTO> findAll();
}
