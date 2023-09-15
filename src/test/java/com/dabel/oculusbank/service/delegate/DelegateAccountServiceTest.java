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
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.BranchService;
import com.dabel.oculusbank.service.CustomerService;
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
    CustomerService customerService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private CustomerDTO savedCustomer;
    private AccountDTO savedAccount;

    private void configCustomersAndAccount(boolean isActiveAccount, boolean isAssociativeAccount, boolean isActiveCustomer) {

        String accountStatus = isActiveAccount ? Status.Active.code() : Status.Pending.code();
        String accountProfile = isAssociativeAccount ? AccountProfile.Associative.name() : AccountProfile.Personal.name();
        String customer2Status = isActiveCustomer ? Status.Active.code() : Status.Pending.code();

        BranchDTO savedBranch = branchService.save(BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());

        CustomerDTO savedCustomer1 = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Active.code())
                .build());

        savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(accountProfile)
                .status(accountStatus)
                .build());

        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer1.getCustomerId(), AccountMemberShip.Owner.name());

        savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("Tom")
                .lastName("Hank")
                .identityNumber("NBE021586")
                .status(customer2Status)
                .build());
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldAddActiveCustomerAsJointOnActivePersonalAccountAndUpdateTheAccountProfileToJoint() {
        //GIVEN
        configCustomersAndAccount(true, false, true);

        //WHEN
        delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber());
        TrunkDTO expected = accountService.findTrunkByCustomerId(savedCustomer.getCustomerId());

        //THEN
        assertThat(expected.getAccountProfile()).isEqualTo(AccountProfile.Joint.name());
        assertThat(expected.getMembership()).isEqualTo(AccountMemberShip.Jointed.name());
    }

    @Test
    void shouldThrowAnAccountNotFoundExceptionWhenTryAddActiveCustomerAsJointOnActiveAccountThatIsNotTrunk() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(AccountProfile.Personal.name())
                .status(Status.Active.code())
                .build());

        //WHEN
        Exception expected = assertThrows(AccountNotFoundException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), "fakeIdentityNumber"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account not found");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddActiveCustomerAsJointOnInactivePersonalAccount() {
        //GIVEN
        configCustomersAndAccount(false, false, true);
        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddActiveCustomerAsJointOnActiveAssociativeAccount() {
        //GIVEN
        configCustomersAndAccount(true, true, true);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddInActiveCustomerAsJointOnActivePersonalAccount() {
        //GIVEN
        configCustomersAndAccount(true, false, false);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addJoint(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer must be active");
    }

    @Test
    void shouldAddActiveCustomerAsAssociateOnActiveAssociativeAccount() {
        //GIVEN
        configCustomersAndAccount(true, true, true);

        //WHEN
        delegateAccountService.addAssociate(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber());
        TrunkDTO expected = accountService.findTrunkByCustomerId(savedCustomer.getCustomerId());

        //THEN
        assertThat(expected.getMembership()).isEqualTo(AccountMemberShip.Associated.name());
    }

    @Test
    void shouldThrowAnAccountNotFoundExceptionWhenTryAddActiveCustomerAsAssociateOnActiveAssociativeAccountThatIsNotTrunk() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(AccountProfile.Associative.name())
                .status(Status.Active.code())
                .build());

        //WHEN
        Exception expected = assertThrows(AccountNotFoundException.class,
                () -> delegateAccountService.addAssociate(savedAccount.getAccountNumber(), "fakeIdentityNumber"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account not found");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddActiveCustomerAsAssociateOnInactiveAssociativeAccount() {
        //GIVEN
        configCustomersAndAccount(false, true, true);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addAssociate(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddActiveCustomerAsAssociateOnANotAssociativeActiveAccount() {
        //GIVEN
        configCustomersAndAccount(true, false, true);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addAssociate(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryAddInactiveCustomerAsAssociateOnActiveAssociativeAccount() {
        //GIVEN
        configCustomersAndAccount(true, true, false);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateAccountService.addAssociate(savedAccount.getAccountNumber(), savedCustomer.getIdentityNumber()));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer must be active");
    }
}
