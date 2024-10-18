package com.SecureSentinel.CardsLoans.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/v1/upload")
public interface DataUploadController {

    @GetMapping("/card")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file);

    @GetMapping("/loan")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<?> uploadLoan(@RequestParam("file") MultipartFile file);

    @GetMapping("/duplicates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> downloadDuplicates(@RequestParam String jobId) throws IOException;
}