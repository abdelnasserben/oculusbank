package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Gender;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    CustomerService customerService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;
    @Autowired
    BranchService branchService;

    private BranchDTO savedBranch;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
        savedBranch = branchService.save(
                BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());
    }

    @Test
    void shouldSaveNewCustomer() {
        //GIVEN
        CustomerDTO customerDTO = CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.Male.name())
                .identityNumber("NBE466754")
                .status(Status.Pending.code())
                .build();

        //WHEN
        CustomerDTO expected = customerService.save(customerDTO);

        //THEN
        assertThat(expected.getCustomerId()).isGreaterThan(0);
        assertThat(expected.getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldRetrieveListOfSavedCustomers() {
        //GIVEN
        customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.Female.name())
                .identityNumber("NBE466754")
                .status(Status.Pending.code())
                .build());

        //WHEN
        List<CustomerDTO> expected = customerService.findAll();

        //THEN
        assertThat(expected.get(0).getFirstName()).isEqualTo("John");
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindCustomerById() {
        //GIVEN
        CustomerDTO savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Pending.code())
                .build());

        //WHEN
        CustomerDTO expected = customerService.findById(savedCustomer.getCustomerId());

        //THEN
        assertThat(expected.getFirstName()).isEqualTo("John");
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindCustomerByIdentityNumber() {
        //GIVEN
        CustomerDTO savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Pending.code())
                .build());

        //WHEN
        CustomerDTO expected = customerService.findByIdentityNumber(savedCustomer.getIdentityNumber());

        //THEN
        assertThat(expected.getFirstName()).isEqualTo("John");
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldThrowACustomerNotFoundExceptionWhenTryToFindCustomerByANotExitsId() {
        //GIVEN

        //WHEN
        Exception expected =  assertThrows(CustomerNotFoundException.class,
                () -> customerService.findById(-12));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer not found");
    }

    @Test
    void shouldThrowACustomerNotFoundExceptionWhenTryToFindCustomerByANotExitsIdentityNumber() {
        //GIVEN

        //WHEN
        Exception expected =  assertThrows(CustomerNotFoundException.class,
                () -> customerService.findByIdentityNumber("fake"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer not found");
    }
}
