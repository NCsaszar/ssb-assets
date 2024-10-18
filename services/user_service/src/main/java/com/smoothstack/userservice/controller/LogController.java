package com.smoothstack.userservice.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    private static final String LOG_FILE_PATH = "logs/upload_report.log";

    @GetMapping("/download")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> downloadLog() {
        Path logPath = Paths.get(LOG_FILE_PATH);
        if (!logPath.toFile().exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(logPath.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + logPath.getFileName().toString() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
