package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.CardAppRequestDTO;
import com.dabel.oculusbank.model.CardAppRequest;
import com.dabel.oculusbank.model.CardAppRequestView;
import org.modelmapper.ModelMapper;

public class CardAppRequestMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static CardAppRequest toEntity(CardAppRequestDTO cardAppRequestDTO) {
        return mapper.map(cardAppRequestDTO, CardAppRequest.class);
    }

    public static CardAppRequestDTO toDTO(CardAppRequest cardAppRequest) {
        return mapper.map(cardAppRequest, CardAppRequestDTO.class);
    }

    public static CardAppRequestDTO viewToDTO(CardAppRequestView cardAppRequestView) {
        return mapper.map(cardAppRequestView, CardAppRequestDTO.class);
    }
}
