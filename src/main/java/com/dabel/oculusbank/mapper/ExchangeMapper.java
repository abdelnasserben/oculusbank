package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.model.Exchange;
import org.modelmapper.ModelMapper;

public class ExchangeMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Exchange toEntity(ExchangeDTO exchangeDTO) {
        return mapper.map(exchangeDTO, Exchange.class);
    }

    public static ExchangeDTO toDTO(Exchange exchange) {
        return mapper.map(exchange, ExchangeDTO.class);
    }

}
