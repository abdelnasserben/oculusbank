package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class BranchDTO {

    private int branchId;
    private String branchName;
    private String branchAddress;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
