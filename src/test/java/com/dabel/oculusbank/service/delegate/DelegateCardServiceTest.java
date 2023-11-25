package com.dabel.oculusbank.service.delegate;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DelegateCardServiceTest {
/*
    @Autowired
    DelegateCardService delegateCardService;
    @Autowired
    BranchService branchService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;


    private AccountDTO getSavedTrunk(boolean isActive, boolean isAssociative) {

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
                .build());

        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMembership.Owner.name());

        return savedAccount;
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldAddNewCardOnActivePersonalAccountAndMakeItPending() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false);
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        //WHEN
        CardDTO expected = delegateCardService.add(cardDTO);

        //THEN
        assertThat(expected.getCardId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldThrowAnAccountNotFoundExceptionWhenTryAddCardOnANotTrunkAccount() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                        .accountName("John Doe")
                        .accountNumber("66398832015")
                        .accountType(AccountType.Saving.name())
                        .accountProfile(AccountProfile.Personal.name())
                        .status(Status.Active.code())
                        .build());

        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        //WHEN
        Exception expected = assertThrows(AccountNotFoundException.class,
                () -> delegateCardService.add(cardDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account not found");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddCardOnAnInactiveActivePersonalAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(false, false);
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateCardService.add(cardDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible to receive a card");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddCardOnActiveAssociativeAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, true);
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateCardService.add(cardDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible to receive a card");
    }

    @Test
    void shouldActivatePendingSavedCard() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false);
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        CardDTO savedCard = delegateCardService.add(cardDTO);

        //WHEN
        CardDTO expected = delegateCardService.activate(savedCard.getCardId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
    }

    @Test
    void shouldDeactivatePendingSavedCard() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk(true, false);
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        CardDTO savedCard = delegateCardService.add(cardDTO);

        //WHEN
        CardDTO expected = delegateCardService.deactivate(savedCard.getCardId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
    }

 */

}