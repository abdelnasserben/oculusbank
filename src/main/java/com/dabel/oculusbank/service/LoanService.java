package com.dabel.oculusbank.service;

import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.exception.LoanNotFoundException;
import com.dabel.oculusbank.mapper.LoanMapper;
import com.dabel.oculusbank.model.Borrower;
import com.dabel.oculusbank.model.Loan;
import com.dabel.oculusbank.model.LoanView;
import com.dabel.oculusbank.repository.BorrowerRepository;
import com.dabel.oculusbank.repository.LoanRepository;
import com.dabel.oculusbank.repository.LoanViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    BorrowerRepository borrowerRepository;
    @Autowired
    LoanViewRepository loanViewRepository;

    public LoanDTO save(LoanDTO loanDTO) {
        Loan loan = loanRepository.save(LoanMapper.toEntity(loanDTO));
        Borrower borrower = Borrower.builder()
                .loanId(loan.getLoanId())
                .customerId(loanDTO.getCustomerId())
                .build();
        borrowerRepository.save(borrower);

        return LoanMapper.toDTO(loan);
    }

    public LoanDTO update(LoanDTO loanDTO) {
        Loan loan = loanRepository.save(LoanMapper.toEntity(loanDTO));
        return LoanMapper.toDTO(loan);
    }

    public LoanDTO findLoanById(int loanId) {
        LoanView loan = loanViewRepository.findById(loanId)
                .orElseThrow(LoanNotFoundException::new);
        return LoanMapper.viewToDTO(loan);
    }

    public List<LoanDTO> findAll() {
        return loanViewRepository.findAll().stream()
                .map(LoanMapper::viewToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> findAllByCustomerIdentityNumber(String customerIdentityNumber) {
        return loanViewRepository.findAllByIdentityNumber(customerIdentityNumber).stream()
                .map(LoanMapper::viewToDTO)
                .collect(Collectors.toList());
    }
}
