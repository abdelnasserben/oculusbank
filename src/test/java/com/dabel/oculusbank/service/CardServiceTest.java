package com.dabel.oculusbank.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CardServiceTest {
/*
    @Autowired
    CardService cardService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;

    private void setSavedAccount() {
        savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("66398832015")
                .accountType(AccountType.Saving.name())
                .status(Status.Pending.code())
                .build());
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveANewCard() {
        //GIVEN
        setSavedAccount();
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build();
        //WHEN
        CardDTO expected = cardService.save(cardDTO);

        //THEN
        assertThat(expected.getCardId()).isGreaterThan(0);
    }

    @Test
    void shouldFindByCardById() {
        //GIVEN
        setSavedAccount();
        CardDTO savedCard = cardService.save(
                CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        CardDTO expected = cardService.findById(savedCard.getCardId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldThrowCardNotFoundExceptionWhenTryFindCardByANotExistsCardId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(CardNotFoundException.class,
                () -> cardService.findById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Card not found");
    }

    @Test
    void findByCardNumber() {
        //GIVEN
        setSavedAccount();
        CardDTO savedCard = cardService.save(
                CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        CardDTO expected = cardService.findByCardNumber(savedCard.getCardNumber());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldThrowCardNotFoundExceptionWhenTryFindCardByANotExistsCardNumber() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(CardNotFoundException.class,
                () -> cardService.findByCardNumber("fake"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Card not found");
    }

    @Test
    void findAllByAccountId() {
        //GIVEN
        setSavedAccount();
        cardService.save(CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        List<CardDTO> expected = cardService.findAllByAccountId(savedAccount.getAccountId());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.get(0).getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

 */
}