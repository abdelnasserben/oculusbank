package com.dabel.oculusbank.service;

import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CustomerServiceTest {
/*
    @Autowired
    CustomerService customerService;
    @Autowired
    BranchService branchService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private BranchDTO savedBranch;

    private void setSavedBranch() {
        savedBranch = branchService.save(BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveNewCustomer() {
        //GIVEN
        setSavedBranch();
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
        setSavedBranch();
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
        setSavedBranch();
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
    void shouldThrowACustomerNotFoundExceptionWhenTryToFindCustomerByANotExitsId() {
        //GIVEN

        //WHEN
        Exception expected =  assertThrows(CustomerNotFoundException.class,
                () -> customerService.findById(-12));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer not found");
    }

    @Test
    void shouldFindCustomerByIdentityNumber() {
        //GIVEN
        setSavedBranch();
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
    void shouldThrowACustomerNotFoundExceptionWhenTryToFindCustomerByANotExitsIdentityNumber() {
        //GIVEN

        //WHEN
        Exception expected =  assertThrows(CustomerNotFoundException.class,
                () -> customerService.findByIdentityNumber("fake"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Customer not found");
    }

 */
}
