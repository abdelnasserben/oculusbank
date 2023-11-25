package com.dabel.oculusbank.service.core.loan;

import com.dabel.oculusbank.app.util.CustomerChecker;
import com.dabel.oculusbank.app.util.LoanCalculator;
import com.dabel.oculusbank.app.util.AccountNumberGenerator;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.core.account.BasicAccountCrudService;
import com.dabel.oculusbank.service.core.customer.CustomerCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanFacadeService {

    @Autowired
    LoanCrudService loanCrudService;
    @Autowired
    CustomerCrudService customerCrudService;
    @Autowired
    BasicAccountCrudService basicAccountCrudService;

    public LoanDTO loan(LoanDTO loanDTO) {

        CustomerDTO customer = customerCrudService.findByIdentityNumber(loanDTO.getIdentityNumber());
        if(!CustomerChecker.isActive(customer))
            throw new IllegalOperationException("Customer must be active");

        LoanCalculator loanCalculator = new LoanCalculator(loanDTO.getIssuedAmount(), loanDTO.getInterestRate(), loanDTO.getDuration());

        AccountDTO account = basicAccountCrudService.save(
                AccountDTO.builder()
                .accountName(customer.getFirstName() + " " + customer.getLastName())
                .accountNumber(AccountNumberGenerator.generate())
                .accountType(AccountType.LOAN.name())
                .currency(Currency.KMF.name())
                .balance(-loanCalculator.getTotalAmountDue())
                .status(Status.PENDING.code())
                .build());

        //TODO: Update loan information before saving
        loanDTO.setCustomerId(customer.getCustomerId());
        loanDTO.setAccountId(account.getAccountId());
        loanDTO.setCurrency(Currency.KMF.name());
        loanDTO.setTotalAmount(loanCalculator.getTotalAmountDue());
        loanDTO.setPerMonthAmount(loanCalculator.getPerMonthAmountDue());
        loanDTO.setStatus(Status.PENDING.code());

        return loanCrudService.save(loanDTO);
    }

    public LoanDTO approve(int operationId) {
        LoanDTO loan = loanCrudService.findById(operationId);
        AccountDTO account = basicAccountCrudService.findByNumber(loan.getAccountNumber());

        loan.setStatus(Status.ACTIVE.code());
        loan.setUpdatedBy("Administrator");
        loan.setUpdatedAt(LocalDateTime.now());

        account.setStatus(Status.ACTIVE.code());
        account.setUpdatedAt(LocalDateTime.now());
        account.setBalance(loan.getTotalAmount());
        basicAccountCrudService.save(account);

        return loanCrudService.update(loan);
    }

    public LoanDTO reject(int operationId, String remarks) {

        LoanDTO loan = loanCrudService.findById(operationId);
        AccountDTO account = basicAccountCrudService.findByNumber(loan.getAccountNumber());

        loan.setStatus(Status.REJECTED.code());
        loan.setFailureReason(remarks);
        loan.setUpdatedBy("Administrator");
        loan.setUpdatedAt(LocalDateTime.now());

        account.setStatus(Status.REJECTED.code());
        account.setUpdatedAt(LocalDateTime.now());
        account.setBalance(loan.getTotalAmount());
        account.setBalance(0);
        basicAccountCrudService.save(account);

        return loanCrudService.update(loan);
    }

    public List<LoanDTO> findAll() {
        return loanCrudService.findAll();
    }

    public LoanDTO findById(int loanId) {
        return loanCrudService.findById(loanId);
    }

    public List<LoanDTO> findAllByCustomerIdentityNumber(String customerIdentityNumber) {
        return loanCrudService.findAllByCustomerIdentityNumber(customerIdentityNumber);
    }
}
