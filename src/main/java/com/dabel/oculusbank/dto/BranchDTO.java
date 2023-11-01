package com.dabel.oculusbank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BranchDTO extends BasicDTO {

    private int branchId;
    @NotBlank
    private String branchName;
    @NotBlank
    private String branchAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
