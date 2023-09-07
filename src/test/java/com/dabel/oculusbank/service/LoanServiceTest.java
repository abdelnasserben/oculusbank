package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.LoanType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.exception.LoanNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LoanServiceTest {

    @Autowired
    LoanService loanService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;
    @Autowired
    BranchService branchService;

    private CustomerDTO savedCustomer;
    private AccountDTO savedAccount;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();

        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());
        savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Pending.code())
                .build());
        savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("66398832015")
                .accountType(AccountType.Current.name())
                .status(Status.Pending.code())
                .build());
    }

    @Test
    void shouldSaveLoan() {
        //GIVEN
        LoanDTO loanDTO = LoanDTO.builder()
                .customerId(savedCustomer.getCustomerId())
                .loanType(LoanType.Gold.name())
                .accountId(savedAccount.getAccountId())
                .status(Status.Pending.code())
                .build();

        //WHEN
        LoanDTO expected = loanService.save(loanDTO);

        //THEN
        assertThat(expected.getLoanId()).isGreaterThan(0);
    }

    @Test
    void shouldFindLoanById() {
        //GIVEN
        LoanDTO loanDTO = LoanDTO.builder()
                .customerId(savedCustomer.getCustomerId())
                .loanType(LoanType.Gold.name())
                .accountId(savedAccount.getAccountId())
                .status(Status.Pending.code())
                .build();
        LoanDTO savedLoan = loanService.save(loanDTO);
        //WHEN
        LoanDTO expected = loanService.findLoanById(savedLoan.getLoanId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.getFirstName()).isEqualTo(savedCustomer.getFirstName());
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldThrowLoanNotFoundExceptionWhenTryFindLoanByANotExistsId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(LoanNotFoundException.class,
                () -> loanService.findLoanById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Loan not found");
    }

    @Test
    void shouldRetrieveListOfAllLoans() {
        //GIVEN
        LoanDTO loanDTO = LoanDTO.builder()
                .customerId(savedCustomer.getCustomerId())
                .loanType(LoanType.Gold.name())
                .accountId(savedAccount.getAccountId())
                .status(Status.Pending.code())
                .build();
        loanService.save(loanDTO);
        //WHEN
        List<LoanDTO> expected = loanService.findAll();

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.get(0).getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldRetrieveAllLoansByCustomerIdentityNumber() {
        //GIVEN
        LoanDTO loanDTO = LoanDTO.builder()
                .customerId(savedCustomer.getCustomerId())
                .loanType(LoanType.Gold.name())
                .accountId(savedAccount.getAccountId())
                .status(Status.Pending.code())
                .build();
        loanService.save(loanDTO);
        //WHEN
        List<LoanDTO> expected = loanService.findAllByCustomerIdentityNumber(savedCustomer.getIdentityNumber());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getFirstName()).isEqualTo(savedCustomer.getFirstName());
    }

}