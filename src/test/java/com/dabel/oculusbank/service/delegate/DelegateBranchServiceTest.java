package com.dabel.oculusbank.service.delegate;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DelegateBranchServiceTest {
/*
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
        double[] assets = {0, 0, 0};
        //WHEN
        BranchDTO expected = delegateBranchService.create(branchDTO, assets);

        //THEN
        assertThat(expected.getBranchId()).isGreaterThan(0);
        List<VaultDTO> vaultDTOS = accountService.findAllVaultsByBranchId(expected.getBranchId());
        assertThat(vaultDTOS.size()).isEqualTo(3);
        assertThat(vaultDTOS.get(0).getCurrency()).isEqualTo(Currency.KMF.name());
        assertThat(vaultDTOS.get(1).getCurrency()).isEqualTo(Currency.EUR.name());
        assertThat(vaultDTOS.get(2).getCurrency()).isEqualTo(Currency.USD.name());
    }

 */
}
