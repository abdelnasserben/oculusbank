package com.dabel.oculusbank.service.core.branch;

import com.dabel.oculusbank.dto.BranchDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BranchCrudService {

    BranchDTO save(BranchDTO branchDTO);

    BranchDTO findById(int branchId);

    List<BranchDTO> findAll();
}
