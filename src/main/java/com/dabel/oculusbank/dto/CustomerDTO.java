package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.custom.validation.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class CustomerDTO {

    private int customerId;
    private int branchId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Gender
    private String gender;
    @NotBlank
    private String identityNumber;
    private String identityType;
    private String identityIssue;
    private LocalDate identityExpiration;
    private LocalDate birthDate;
    private String birthPlace;
    private String nationality;
    private String residence;
    private String address;
    private String postCode;
    private String phone;
    private String email;
    private String profession;
    private String profilePicture;
    private String signaturePicture;
    private String identityPicture;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
