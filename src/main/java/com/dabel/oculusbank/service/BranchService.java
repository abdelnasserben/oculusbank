package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.exception.BranchNotFoundException;
import com.dabel.oculusbank.mapper.BranchMapper;
import com.dabel.oculusbank.model.Branch;
import com.dabel.oculusbank.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    public BranchDTO save(BranchDTO branchDTO) {
        Branch branch = branchRepository.save(BranchMapper.toEntity(branchDTO));
        return BranchMapper.toDTO(branch);
    }

    public List<BranchDTO> findAll() {
        return branchRepository.findAll().stream()
                .map(BranchService::formatStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    public BranchDTO findById(int branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(BranchNotFoundException::new);

        return formatStatusToNameAndGetDTO(branch);
    }

    private static BranchDTO formatStatusToNameAndGetDTO(Branch branch) {
        branch.setStatus(Status.nameOf(branch.getStatus()));
        return BranchMapper.toDTO(branch);
    }
}
