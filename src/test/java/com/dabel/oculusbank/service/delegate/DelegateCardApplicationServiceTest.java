package com.dabel.oculusbank.service.delegate;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DelegateCardApplicationServiceTest {
/*
    @Autowired
    DelegateCardApplicationService delegateCardApplicationService;
    @Autowired
    BranchService branchService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;


    private AccountDTO getSavedTrunk(boolean isActive, boolean isAssociative, double balance) {

        String accountStatus = isActive ? Status.Active.code() : Status.Pending.code();
        String accountProfile = isAssociative ? AccountProfile.Associative.name() : AccountProfile.Personal.name();

        BranchDTO savedBranch = branchService.save(BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());

        CustomerDTO savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Active.code())
                .build());

        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(accountProfile)
                .status(accountStatus)
                .balance(balance)
                .build());

        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMembership.Owner.name());

        return savedAccount;
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSendCardApplicationRequestAndMakeItPendingForAnActivePersonalAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false, 25000);
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .customerIdentityNumber("NBE466754")
                .build();

        //WHEN
        CardApplicationDTO expected = delegateCardApplicationService.sendRequest(cardApplicationDTO);

        //THEN
        assertThat(expected.getRequestId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldThrowAnAccountNotFoundExceptionWhenTrySendRequestForANotTrunkAccount() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(AccountProfile.Personal.name())
                .status(Status.Active.code())
                .balance(25000)
                .build());

        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();

        //WHEN
        Exception expected = assertThrows(AccountNotFoundException.class,
                () -> delegateCardApplicationService.sendRequest(cardApplicationDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account not found");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTrySendRequestForAnInActivePersonalAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(false, false, 25000);
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .customerIdentityNumber("NBE466754")
                .build();

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateCardApplicationService.sendRequest(cardApplicationDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTrySendRequestForActiveAssociativeAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, true, 25000);
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .customerIdentityNumber("NBE466754")
                .build();

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateCardApplicationService.sendRequest(cardApplicationDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTrySendRequestOnActivePersonalAccountWithLessBalanceThanCardApplicationFees() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false, 100);
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .customerIdentityNumber("NBE466754")
                .build();

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> delegateCardApplicationService.sendRequest(cardApplicationDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient for application fees");
    }

    @Test
    void shouldApproveAPendingCardApplicationRequest() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false, 25000);
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .customerIdentityNumber("NBE466754")
                .build();
        CardApplicationDTO savedRequestApp = delegateCardApplicationService.sendRequest(cardApplicationDTO);

        //WHEN
        CardApplicationDTO expected = delegateCardApplicationService.approveApplication(savedRequestApp.getRequestId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(20000);
    }

    @Test
    void shouldRejectAPendingCardApplicationRequest() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false, 25000);
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .customerIdentityNumber("NBE466754")
                .build();
        CardApplicationDTO savedRequestApp = delegateCardApplicationService.sendRequest(cardApplicationDTO);

        //WHEN
        CardApplicationDTO expected = delegateCardApplicationService.rejectApplication(savedRequestApp.getRequestId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
    }

 */
}