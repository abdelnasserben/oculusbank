package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.VaultDTO;
import com.dabel.oculusbank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class DelegateBranchServiceTest {

    @Autowired
    DelegateBranchService delegateBranchService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldCreateBranchWithHisVaultsAllInOnce() {
        //GIVEN
        BranchDTO branchDTO = BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build();
        //WHEN
        BranchDTO expected = delegateBranchService.create(branchDTO);

        //THEN
        assertThat(expected.getBranchId()).isGreaterThan(0);
        List<VaultDTO> vaultDTOS = accountService.findAllVaultsByBranchId(expected.getBranchId());
        assertThat(vaultDTOS.size()).isEqualTo(3);
        assertThat(vaultDTOS.get(0).getCurrency()).isEqualTo(Currency.KMF.name());
        assertThat(vaultDTOS.get(1).getCurrency()).isEqualTo(Currency.EUR.name());
        assertThat(vaultDTOS.get(2).getCurrency()).isEqualTo(Currency.USD.name());
    }
}
