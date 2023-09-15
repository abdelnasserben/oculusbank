package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.LoanType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.BranchService;
import com.dabel.oculusbank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DelegateLoanServiceTest {

    @Autowired
    DelegateLoanService delegateLoanService;
    @Autowired
    BranchService branchService;
    @Autowired
    CustomerService customerService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private LoanDTO loanDTO;

    private void configSavedCustomerAndLoanDTO(boolean isActiveCustomer) {

        String customerStatus = isActiveCustomer ? Status.Active.code() : Status.Pending.code();
        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());
        CustomerDTO savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(customerStatus)
                .build());
        loanDTO = LoanDTO.builder()
                .loanType(LoanType.Gold.name())
                .customerId(savedCustomer.getCustomerId())
                .issuedAmount(15239)
                .interestRate(1.24)
                .duration(3)
                .reason("Sample reason")
                .build();
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldInitLoan() {
        //GIVEN
        configSavedCustomerAndLoanDTO(true);

        //WHEN
        LoanDTO expected = delegateLoanService.loan(loanDTO);

        //THEN
        assertThat(expected.getLoanId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
        assertThat(expected.getTotalAmount()).isEqualTo(15427.96);
        assertThat(expected.getPerMonthAmount()).isEqualTo(5142.66);

    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryInitLoanWithAnInactiveCustomer() {
        //GIVEN
        configSavedCustomerAndLoanDTO(false);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateLoanService.loan(loanDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer must be active");
    }

    @Test
    void shouldApprovePendingSavedLoan() {
        //GIVEN
        configSavedCustomerAndLoanDTO(true);
        LoanDTO savedLoan = delegateLoanService.loan(loanDTO);

        //WHEN
        LoanDTO expected = delegateLoanService.approve(savedLoan.getLoanId());

        //THEN
        assertThat(expected.getLoanId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
    }

    @Test
    void shouldRejectPendingSavedLoan() {
        //GIVEN
        configSavedCustomerAndLoanDTO(true);
        LoanDTO savedLoan = delegateLoanService.loan(loanDTO);

        //WHEN
        LoanDTO expected = delegateLoanService.reject(savedLoan.getLoanId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
    }
}