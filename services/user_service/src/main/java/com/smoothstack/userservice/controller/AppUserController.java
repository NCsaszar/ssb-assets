package com.smoothstack.userservice.controller;

import com.smoothstack.userservice.dto.AppUserDTO;
import com.smoothstack.userservice.dto.UserResponseDTO;
import com.smoothstack.userservice.service.AppUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<AppUserDTO> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        return appUserService.getAllUsersWithPagination(pageable);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.userId")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(appUserService.getUserDtoById(userId));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
        String responseMessage = appUserService.deleteUserById(userId);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserDTO> createUser(@Valid @RequestBody AppUserDTO userDTO) {
        return ResponseEntity.ok(appUserService.createUser(userDTO));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.userId")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer userId, @Valid @RequestBody AppUserDTO userDTO) {
        return ResponseEntity.ok(appUserService.updateUser(userId, userDTO));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<AppUserDTO>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable) { // Pageable parameter to support pagination

        Page<AppUserDTO> users = appUserService.searchUsers(username, email, firstName, lastName, pageable);
        return ResponseEntity.ok(users);
    }
}
