package com.dabel.oculusbank.service.core.card;

import com.dabel.oculusbank.dto.CardDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardCrudService {

    CardDTO save(CardDTO cardDTO);

    CardDTO findById(int cardId);

    CardDTO findByCardNumber(String cardNumber);

    List<CardDTO> findAllByAccountId(int accountId);
}
