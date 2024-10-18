package com.smoothstack.branchservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class BankerDTO {

    private Integer bankerId;

    @NotNull
    private Integer branchId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid phone number format. Use xxx-xxx-xxxx.")
    private String phoneNumber;

    @NotBlank
    @Email
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank
    private String jobTitle;

    private List<AppointmentDTO> appointments;  // slated for review on removal

}