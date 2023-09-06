package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.VaultDTO;
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
    void shouldCreateBranchWithHerAccountsAllInOnce() {
        //GIVEN
        BranchDTO branchDTO = BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build();
        //WHEN
        BranchDTO expected = delegateBranchService.createWithOwnAccountsAtOnce(branchDTO);

        //THEN
        assertThat(expected.getBranchId()).isGreaterThan(0);
        List<VaultDTO> vaultDTOS = accountService.findAllVaultsByBranchId(expected.getBranchId());
        assertThat(vaultDTOS.size()).isEqualTo(3);
    }
}
