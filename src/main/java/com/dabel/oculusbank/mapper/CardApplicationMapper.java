package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.model.CardApplication;
import com.dabel.oculusbank.model.CardApplicationView;
import org.modelmapper.ModelMapper;

public class CardApplicationMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static CardApplication toEntity(CardApplicationDTO cardApplicationDTO) {
        return mapper.map(cardApplicationDTO, CardApplication.class);
    }

    public static CardApplicationDTO toDTO(CardApplication cardApplication) {
        return mapper.map(cardApplication, CardApplicationDTO.class);
    }

    public static CardApplicationDTO viewToDTO(CardApplicationView cardApplicationView) {
        return mapper.map(cardApplicationView, CardApplicationDTO.class);
    }
}
