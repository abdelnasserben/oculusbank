package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.model.Branch;
import org.modelmapper.ModelMapper;

public class BranchMapper {
    private static final ModelMapper mapper = new ModelMapper();

    public static Branch toEntity(BranchDTO branchDTO) {
        return mapper.map(branchDTO, Branch.class);
    }

    public static BranchDTO toDTO(Branch branch) {
        return mapper.map(branch, BranchDTO.class);
    }
}
