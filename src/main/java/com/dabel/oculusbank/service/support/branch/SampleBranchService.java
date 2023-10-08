package com.dabel.oculusbank.service.support.branch;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.exception.BranchNotFoundException;
import com.dabel.oculusbank.mapper.BranchMapper;
import com.dabel.oculusbank.model.Branch;
import com.dabel.oculusbank.repository.BranchRepository;
import com.dabel.oculusbank.service.core.branch.BranchCruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SampleBranchService implements BranchCruService {

    @Autowired
    BranchRepository branchRepository;

    @Override
    public BranchDTO save(BranchDTO branchDTO) {

        Branch branch = branchRepository.save(BranchMapper.toEntity(branchDTO));
        return BranchMapper.toDTO(branch);
    }

    @Override
    public BranchDTO findById(int branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(BranchNotFoundException::new);

        return BranchMapper.toDTO(branch);
    }

    @Override
    public List<BranchDTO> findAll() {

        return branchRepository.findAll().stream()
                .map(BranchMapper::toDTO)
                .collect(Collectors.toList());
    }

    private static BranchDTO formatStatusAndMapToDto(Branch branch) {

        branch.setStatus(Status.nameOf(branch.getStatus()));
        return BranchMapper.toDTO(branch);
    }
}
