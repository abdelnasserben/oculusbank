package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.service.delegate.DelegateCustomerService;
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
        CustomerDTO expected = delegateCustomerService.createWithOwnAccountsAtOnce(customerDTO, true);

        //THEN
        assertThat(expected.getCustomerId()).isGreaterThan(0);
        List<TrunkDTO> trunks = accountService.findAllTrunksByCustomerId(expected.getCustomerId());
        assertThat(trunks.size()).isEqualTo(1);
        assertThat(trunks.get(0).getCurrency()).isEqualTo(Currency.KMF.name());
    }

    @Test
    void shouldCreateCustomerWithoutAccountAtOnce() {
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
        CustomerDTO expected = delegateCustomerService.createWithOwnAccountsAtOnce(customerDTO, false);

        //THEN
        assertThat(expected.getCustomerId()).isGreaterThan(0);
        List<TrunkDTO> trunks = accountService.findAllTrunksByCustomerId(expected.getCustomerId());
        assertThat(trunks.size()).isEqualTo(0);

    }
}
