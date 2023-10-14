package com.dabel.oculusbank.service.core.card;

import com.dabel.oculusbank.dto.CardAppRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardApplicationCrudService {

    CardAppRequestDTO save(CardAppRequestDTO cardAppRequestDTO);

    CardAppRequestDTO findById(int requestId);

    List<CardAppRequestDTO> findAll();
}
