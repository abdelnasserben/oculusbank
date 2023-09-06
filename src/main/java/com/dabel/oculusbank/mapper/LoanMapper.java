package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.model.Loan;
import com.dabel.oculusbank.model.LoanView;
import org.modelmapper.ModelMapper;

public class LoanMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Loan toEntity(LoanDTO loanDTO) {
        return mapper.map(loanDTO, Loan.class);
    }

    public static LoanDTO toDTO(Loan loan) {
        return mapper.map(loan, LoanDTO.class);
    }

    public static LoanDTO viewToDTO(LoanView loanView) {
        return mapper.map(loanView, LoanDTO.class);
    }
}
