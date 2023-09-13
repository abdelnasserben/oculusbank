package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountMemberShip;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
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
public class DelegateAccountServiceTest {

    @Autowired
    DelegateAccountService delegateAccountService;
    @Autowired
    BranchService branchService;
    @Autowired
    AccountService accountService;
    @Autowired
    DelegateCustomerService delegateCustomerService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldAddAJointCustomerOnPersonalAccount() {
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

        CustomerDTO savedCustomerWithAccount = delegateCustomerService.create(customerDTO, AccountType.Saving.name(), AccountProfile.Personal.name(), AccountMemberShip.Owner.name());

        CustomerDTO savedCustomerWithoutAccount = delegateCustomerService.create(
                CustomerDTO.builder()
                        .branchId(savedBranch.getBranchId())
                        .firstName("Tom")
                        .lastName("Hank")
                        .identityNumber("NBE021586")
                        .build());

        TrunkDTO trunkSaved = accountService.findTrunkByCustomerId(savedCustomerWithAccount.getCustomerId());

        //WHEN
        delegateAccountService.addJoint(trunkSaved.getAccountNumber(), savedCustomerWithoutAccount.getIdentityNumber());
        TrunkDTO expected = accountService.findTrunkByCustomerId(savedCustomerWithoutAccount.getCustomerId());

        //THEN
        assertThat(expected.getAccountProfile()).isEqualTo(AccountProfile.Joint.name());
        assertThat(expected.getMembership()).isEqualTo(AccountMemberShip.Jointed.name());
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddJointOnANonActiveAccount() {
        //GIVEN
        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());

        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(AccountProfile.Personal.name())
                .status(Status.Pending.code())
                .build());

        CustomerDTO savedCustomer= delegateCustomerService.create(
                CustomerDTO.builder()
                        .branchId(savedBranch.getBranchId())
                        .firstName("Tom")
                        .lastName("Hank")
                        .identityNumber("NBE021586")
                        .build());
        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddJointOnAnAssociativeAccount() {
        //GIVEN
        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());

        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                        .accountName("John Doe")
                        .accountNumber("123456789")
                        .accountType(AccountType.Saving.name())
                        .accountProfile(AccountProfile.Associative.name())
                        .status(Status.Active.code())
                        .build());

        CustomerDTO savedCustomer= delegateCustomerService.create(
                CustomerDTO.builder()
                        .branchId(savedBranch.getBranchId())
                        .firstName("Tom")
                        .lastName("Hank")
                        .identityNumber("NBE021586")
                        .build());
        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddJointOnAccountWithNonActiveCustomer() {
        //GIVEN
        BranchDTO savedBranch = branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());

        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                        .accountName("John Doe")
                        .accountNumber("123456789")
                        .accountType(AccountType.Saving.name())
                        .accountProfile(AccountProfile.Associative.name())
                        .status(Status.Active.code())
                        .build());

        CustomerDTO savedCustomer= delegateCustomerService.create(
                CustomerDTO.builder()
                        .branchId(savedBranch.getBranchId())
                        .firstName("Tom")
                        .lastName("Hank")
                        .identityNumber("NBE021586")
                        .status(Status.Pending.code())
                        .build());
        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldAddAnAssociateCustomerOnAssociativeAccount() {
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
        CustomerDTO savedCustomerWithAccount = delegateCustomerService.create(customerDTO, AccountType.Saving.name(), AccountProfile.Associative.name(), AccountMemberShip.Associated.name());


        CustomerDTO savedCustomerWithoutAccount = delegateCustomerService.create(
                CustomerDTO.builder()
                        .branchId(savedBranch.getBranchId())
                        .firstName("Tom")
                        .lastName("Hank")
                        .identityNumber("NBE021586")
                        .build());

        TrunkDTO trunkSaved = accountService.findTrunkByCustomerId(savedCustomerWithAccount.getCustomerId());

        //WHEN
        delegateAccountService.addAssociate(trunkSaved.getAccountNumber(), savedCustomerWithoutAccount.getIdentityNumber());
        TrunkDTO expected = accountService.findTrunkByCustomerId(savedCustomerWithoutAccount.getCustomerId());

        //THEN
        assertThat(expected.getAccountProfile()).isEqualTo(AccountProfile.Associative.name());
        assertThat(expected.getMembership()).isEqualTo(AccountMemberShip.Associated.name());
    }
}
