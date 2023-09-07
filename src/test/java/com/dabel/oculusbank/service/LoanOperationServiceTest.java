package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.LoanType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.LoanDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoanOperationServiceTest {

    @Autowired
    LoanOperationService loanOperationService;
    @Autowired
    BranchService branchService;
    @Autowired
    CustomerService customerService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private CustomerDTO savedCustomer;

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
    }

    @Test
    void shouldInitLoan() {
        //GIVEN
        String loanType = LoanType.Gold.name(),reason = "Sample reason";
        double issuedAmount = 15239, interestRate = 1.24;
        int duration = 3;

        //WHEN
        LoanDTO expected = loanOperationService.loan(savedCustomer.getCustomerId(), loanType, issuedAmount, interestRate, duration, reason);

        //THEN
        assertThat(expected.getLoanId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
        assertThat(expected.getTotalAmount()).isEqualTo(15427.96);
        assertThat(expected.getPerMonthAmount()).isEqualTo(5142.66);

    }

    @Test
    void shouldApproveALoan() {
        //GIVEN
        String loanType = LoanType.Gold.name(),reason = "Sample reason";
        double issuedAmount = 15239, interestRate = 1.24;
        int duration = 3;
        LoanDTO savedLoan = loanOperationService.loan(savedCustomer.getCustomerId(), loanType, issuedAmount, interestRate, duration, reason);

        //WHEN
        LoanDTO expected = loanOperationService.approve(savedLoan.getLoanId());

        //THEN
        assertThat(expected.getLoanId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
    }

    @Test
    void shouldRejectALoan() {
        //GIVEN
        String loanType = LoanType.Gold.name(),reason = "Sample reason";
        double issuedAmount = 15239, interestRate = 1.24;
        int duration = 3;
        LoanDTO savedLoan = loanOperationService.loan(savedCustomer.getCustomerId(), loanType, issuedAmount, interestRate, duration, reason);

        //WHEN
        LoanDTO expected = loanOperationService.reject(savedLoan.getLoanId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
    }
}