package com.dabel.oculusbank.service.delegate;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DelegateCustomerServiceTest {
/*
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
    void shouldCreateNewCustomerWithoutAccountAndMakeHimActive() {
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
    void shouldCreateCustomerWithHisAccountAllInOnceAndMakeThemActives() {
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
        CustomerDTO expected = delegateCustomerService.create(customerDTO, AccountType.Saving.name(), AccountProfile.Personal.name(), AccountMembership.Owner.name());

        //THEN
        assertThat(expected.getCustomerId()).isGreaterThan(0);
        TrunkDTO trunks = accountService.findTrunkByCustomerId(expected.getCustomerId());
        assertThat(trunks.getAccountProfile()).isEqualTo(AccountProfile.Personal.name());
        assertThat(trunks.getStatus()).isEqualTo(Status.Active.name());
    }

 */
}
