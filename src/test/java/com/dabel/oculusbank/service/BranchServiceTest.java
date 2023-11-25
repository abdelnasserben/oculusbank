package com.dabel.oculusbank.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BranchServiceTest {
/*
    @Autowired
    BranchService branchService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveNewBranch() {
        //GIVEN
        BranchDTO branch = BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build();

        //WHEN
        BranchDTO expected = branchService.save(branch);

        //THEN
        assertThat(expected.getBranchId()).isGreaterThan(0);
    }

    @Test
    void shouldRetrieveListOfSavedBranches() {
        //GIVEN
        branchService.save(BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());

        //WHEN
        List<BranchDTO> expected = branchService.findAll();

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getBranchName()).isEqualTo("HQ");
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Active.name());
    }

    @Test
    void shouldFindABranchById() {
        //GIVEN
        BranchDTO branch = BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build();
        BranchDTO savedBranch = branchService.save(branch);

        //WHEN
        BranchDTO expected = branchService.findById(savedBranch.getBranchId());

        //THEN
        assertThat(expected.getBranchId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Active.name());
    }

    @Test
    void shouldThrowABranchNotFoundExceptionWhenTryToFindBranchByANotExitsId() {
        //GIVEN

        //WHEN
        Exception expected =  assertThrows(BranchNotFoundException.class,
                () -> branchService.findById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Branch not found");
    }

 */
}
