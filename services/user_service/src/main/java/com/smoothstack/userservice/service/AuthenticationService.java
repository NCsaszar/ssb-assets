package com.smoothstack.userservice.service;

import com.smoothstack.userservice.model.*;
import com.smoothstack.userservice.repository.AppUserRepository;
import com.smoothstack.userservice.util.EncryptionUtil;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityExistsException("Username already exists");
        }

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new EntityExistsException("Email already exists");
        }

        var user = AppUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .role(Role.CUSTOMER)
                .build();

        AppUser savedUser = repository.save(user);

        // Generate verification token
        String token = verificationTokenService.generateToken();
        verificationTokenService.createVerificationToken(savedUser.getUserId(), token);

        // Construct the verification link
        String verificationLink = "http://localhost:8085/api/v1/auth/confirm?token=" + EncryptionUtil.encrypt(token);

        // Send welcome email with verification link
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername(), verificationLink);

        // Generate JWT token with userDetails, role, and userId
        var jwtToken = jwtService.generateToken(savedUser, savedUser.getRole().name(), savedUser.getUserId());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(savedUser.getUserId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new LockedException("Account is inactive. Access denied.");
        }

        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account is locked. Please try again later.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            user.resetFailedLoginAttempts(); // Resetting the failed attempts on successful login
        } catch (BadCredentialsException e) {
            user.incrementFailedLoginAttempts();
            repository.save(user);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Generate JWT token after successful authentication
        var jwtToken = jwtService.generateToken(user, user.getRole().name(), user.getUserId());

        // Return the authentication response with the JWT token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUserId())
                .build();
    }
}
