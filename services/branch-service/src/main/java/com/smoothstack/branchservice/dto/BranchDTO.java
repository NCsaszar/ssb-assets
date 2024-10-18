package com.smoothstack.branchservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BranchDTO {


    private Integer branchId;

    @NotBlank
    @Size(min = 3, max = 10)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String branchCode;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String branchName;

    @NotNull
    private Integer branchManager;

    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid phone number format. Use xxx-xxx-xxxx.")
    private String phoneNumber;

    @NotBlank
    @Email
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String address1;

    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String address2;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String city;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String state;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
