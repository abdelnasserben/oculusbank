package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountMemberShip;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DelegateCustomerServiceTest {

    @Autowired
    DelegateCustomerService delegateCustomerService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;
    @Autowired
    BranchService branchService;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldCreateAnActiveCustomerWithoutAccount() {
        //GIVEN
        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());

        CustomerDTO customerDTO = CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .build();
        //WHEN
        CustomerDTO expected = delegateCustomerService.create(customerDTO);

        //THEN
        assertThat(expected.getCustomerId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Active.code());
    }

    @Test
    void shouldCreateCustomerWithHisAccountAllInOnce() {
        //GIVEN
        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());

        CustomerDTO customerDTO = CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Pending.code())
                .build();
        //WHEN
        CustomerDTO expected = delegateCustomerService.create(customerDTO, AccountType.Saving.name(), AccountProfile.Personal.name(), AccountMemberShip.Owner.name());

        //THEN
        assertThat(expected.getCustomerId()).isGreaterThan(0);
        TrunkDTO trunks = accountService.findTrunkByCustomerId(expected.getCustomerId());
        assertThat(trunks.getAccountProfile()).isEqualTo(AccountProfile.Personal.name());
        assertThat(trunks.getMembership()).isEqualTo(AccountMemberShip.Owner.name());
    }
}
