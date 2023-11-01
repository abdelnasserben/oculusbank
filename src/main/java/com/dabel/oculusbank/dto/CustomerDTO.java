package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.custom.validation.Country;
import com.dabel.oculusbank.app.custom.validation.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerDTO extends  BasicDTO {

    private int customerId;
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
    @Country
    private String nationality;
    @Country
    private String residence;
    private String address;
    private String postCode;
    private String phone;
    private String email;
    private String profession;
    private String profilePicture;
    private String signaturePicture;
    private String identityPicture;
}
