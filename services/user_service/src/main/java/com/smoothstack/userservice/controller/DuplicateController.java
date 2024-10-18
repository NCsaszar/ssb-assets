package com.smoothstack.userservice.controller;

import com.smoothstack.userservice.service.DuplicateCheckService;
import com.smoothstack.userservice.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/duplicates")
public class DuplicateController {

    @Autowired
    private DuplicateCheckService duplicateCheckService;

    @Autowired
    private FileUploadService fileUploadService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<Map<String, String>> getPendingDuplicates() {
        Map<String, String> pending = duplicateCheckService.getPendingDuplicates();
        return ResponseEntity.ok(pending);
    }

    @PostMapping("/resolve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> resolveDuplicates(@RequestBody Map<String, String> decisions) {
        decisions.forEach((username, decision) -> {
            System.out.println("Resolving duplicate for: " + username + " with decision: " + decision);
            duplicateCheckService.resolveDuplicate(username, decision);
        });

        try {
            fileUploadService.retriggerJob();
            duplicateCheckService.clearDuplicateCache();
            return ResponseEntity.ok("Duplicates resolved and job retriggered");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retriggering job: " + e.getMessage());
        }
    }

}
