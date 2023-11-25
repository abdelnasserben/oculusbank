package com.dabel.oculusbank.service.core.loan;

import com.dabel.oculusbank.dto.LoanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanCrudService {

    LoanDTO save(LoanDTO loanDTO);

    LoanDTO update(LoanDTO loanDTO);

    LoanDTO findById(int loanId);

    List<LoanDTO> findAll();

    List<LoanDTO> findAllByCustomerIdentityNumber(String customerIdentityNumber);
}
