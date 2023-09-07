package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.ChequeDTO;
import com.dabel.oculusbank.model.Cheque;
import com.dabel.oculusbank.model.ChequeView;
import org.modelmapper.ModelMapper;

public class ChequeMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Cheque toEntity(ChequeDTO chequeDTO) {
        return mapper.map(chequeDTO, Cheque.class);
    }

    public static ChequeDTO toDTO(Cheque cheque) {
        return mapper.map(cheque, ChequeDTO.class);
    }

    public static ChequeDTO viewToDTO(ChequeView chequeView) {
        return mapper.map(chequeView, ChequeDTO.class);
    }
}
