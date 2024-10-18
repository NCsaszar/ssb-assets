package com.smoothstack.userservice.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[^a-zA-Z0-9 ].*", message = "Password must contain at least one special character")
    @Pattern(regexp = "^[^\\s]+$", message = "Password must not contain spaces")
    private String password;
}
