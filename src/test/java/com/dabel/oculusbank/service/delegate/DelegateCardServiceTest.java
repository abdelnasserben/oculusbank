package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.*;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DelegateCardServiceTest {

    @Autowired
    DelegateCardService delegateCardService;
    @Autowired
    BranchService branchService;
    @Autowired
    DelegateCustomerService delegateCustomerService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;


    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldAddNewCardAndMakeItPending() {
        //GIVEN
        TrunkDTO savedAccount = getSavedTrunk();

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
    void shouldThrowAnIllegalOperationExceptionWhenTrySaveCardOnANonactiveActive() {
        //GIVEN
        TrunkDTO savedAccount = getSavedTrunk();
        savedAccount.setStatus(Status.Pending.code());
        accountService.save(savedAccount);

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
    void shouldThrowAnIllegalOperationExceptionWhenTrySaveCardOnAnAssociativeAccount() {
        //GIVEN
        TrunkDTO savedAccount = getSavedTrunk();
        savedAccount.setStatus(Status.Active.code());
        savedAccount.setAccountProfile(AccountProfile.Associative.name());
        accountService.save(savedAccount);

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
    void shouldThrowAnAccountNotFoundExceptionWhenTrySaveCardOnANotTrunkAccount() {
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
    void shouldApprovePendingSavedCard() {
        //GIVEN
        TrunkDTO savedAccount = getSavedTrunk();

        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        CardDTO savedCard = delegateCardService.add(cardDTO);

        //WHEN
        CardDTO expected = delegateCardService.approve(savedCard.getCardId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
    }

    @Test
    void shouldRejectPendingSavedCard() {
        //GIVEN
        TrunkDTO savedAccount = getSavedTrunk();

        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        CardDTO savedCard = delegateCardService.add(cardDTO);

        //WHEN
        CardDTO expected = delegateCardService.reject(savedCard.getCardId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
    }

    private TrunkDTO getSavedTrunk() {
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

        CustomerDTO savedCustomer = delegateCustomerService.create(customerDTO, AccountType.Saving.name(), AccountProfile.Personal.name(), AccountMemberShip.Owner.name());
        return accountService.findTrunkByCustomerId(savedCustomer.getCustomerId());
    }

}