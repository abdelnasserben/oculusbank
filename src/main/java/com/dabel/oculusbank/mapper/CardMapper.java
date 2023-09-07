package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.model.Card;
import com.dabel.oculusbank.model.CardView;
import org.modelmapper.ModelMapper;

public class CardMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Card toEntity(CardDTO cardDTO) {
        return mapper.map(cardDTO, Card.class);
    }

    public static CardDTO toDTO(Card card) {
        return mapper.map(card, CardDTO.class);
    }

    public static CardDTO viewToDTO(CardView cardView) {
        return mapper.map(cardView, CardDTO.class);
    }
}
