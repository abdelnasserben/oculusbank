package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.Generator;
import com.dabel.oculusbank.app.LoanCalculator;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CustomerService;
import com.dabel.oculusbank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DelegateLoanService implements OperationAcknowledgment<LoanDTO> {

    @Autowired
    LoanService loanService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;

    public LoanDTO loan(LoanDTO loanDTO) {

        CustomerDTO customer = customerService.findById(loanDTO.getCustomerId());
        LoanCalculator loanCalculator = new LoanCalculator(loanDTO.getIssuedAmount(), loanDTO.getInterestRate(), loanDTO.getDuration());

        AccountDTO account = accountService.save(
                AccountDTO.builder()
                .accountName(customer.getFirstName() + " " + customer.getLastName())
                .accountNumber(Generator.generateAccountNumber())
                .accountType(AccountType.Loan.name())
                .currency(Currency.KMF.name())
                .balance(-loanCalculator.getTotalAmountDue())
                .status(Status.Pending.code())
                .build());
//
//
//        LoanDTO loan = LoanDTO.builder()
//                .customerId(customer.getCustomerId())
//                .loanType(loanType)
//                .accountId(account.getAccountId())
//                .currency(Currency.KMF.name())
//                .issuedAmount(issuedAmount)
//                .interestRate(interestRate)
//                .duration(duration)
//                .totalAmount(loanCalculator.getTotalAmountDue())
//                .perMonthAmount(loanCalculator.getPerMonthAmountDue())
//                .reason(reason)
//                .status(Status.Pending.code())
//                .build();

        //TODO: Update loan information before saving
        loanDTO.setAccountId(account.getAccountId());
        loanDTO.setCurrency(Currency.KMF.name());
        loanDTO.setTotalAmount(loanCalculator.getTotalAmountDue());
        loanDTO.setPerMonthAmount(loanCalculator.getPerMonthAmountDue());
        loanDTO.setStatus(Status.Pending.code());

        return loanService.save(loanDTO);
    }

    @Override
    public LoanDTO approve(int operationId) {
        LoanDTO loan = loanService.findLoanById(operationId);
        AccountDTO account = accountService.findByNumber(loan.getAccountNumber());

        loan.setStatus(Status.Approved.code());
        loan.setUpdatedBy("Administrator");
        loan.setUpdatedAt(LocalDateTime.now());

        account.setStatus(Status.Approved.code());
        account.setUpdatedAt(LocalDateTime.now());
        account.setBalance(loan.getTotalAmount());
        accountService.save(account);

        return loanService.update(loan);
    }

    @Override
    public LoanDTO reject(int operationId, String remarks) {

        LoanDTO loan = loanService.findLoanById(operationId);
        AccountDTO account = accountService.findByNumber(loan.getAccountNumber());

        loan.setStatus(Status.Rejected.code());
        loan.setFailureReason(remarks);
        loan.setUpdatedBy("Administrator");
        loan.setUpdatedAt(LocalDateTime.now());

        account.setStatus(Status.Rejected.code());
        account.setUpdatedAt(LocalDateTime.now());
        account.setBalance(loan.getTotalAmount());
        account.setBalance(0);
        accountService.save(account);

        return loanService.update(loan);
    }
}