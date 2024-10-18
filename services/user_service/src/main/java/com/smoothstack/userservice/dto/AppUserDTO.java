package com.smoothstack.userservice.dto;

import com.smoothstack.userservice.util.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class AppUserDTO {

    private Integer userId;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @ValidPassword
    private String password;

    @ValidPassword
    private String newPassword;

    @Email(message = "Email should be valid and contain at least one '@' and one '.'")
    private String email;

    private String firstName;
    private String lastName;
    private Boolean isVerified;
    private Boolean isActive;
    private Date dateCreated;
    private Date dateModified;
    private Date dateOfBirth;
    private Long phoneNumber;
    private String address;
    private String secretQuestion;
    private String secretAnswer;
    private String role;
    private int failedLoginAttempts;
    private Date lockTime;
}
