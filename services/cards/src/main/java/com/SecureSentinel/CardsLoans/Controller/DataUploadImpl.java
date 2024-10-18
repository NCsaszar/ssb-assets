package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.Batch.BatchConfiguration;
import com.SecureSentinel.CardsLoans.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@RestController
public class DataUploadImpl implements DataUploadController {

    private final UploadService uploadService;

    @Autowired
    public DataUploadImpl(UploadService uploadService, BatchConfiguration batchConfiguration) {
        this.uploadService = uploadService;
    }

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        if (!uploadService.validateFileFormat(file)) {
            return ResponseEntity.badRequest().body("Invalid file format. Only CSV files are allowed.");
        }
        if (!uploadService.validateFileSize(file)) {
            return ResponseEntity.badRequest().body("File size exceeds the maximum limit of 50MB.");
        }


        String jobId = uploadService.startRunJob(file);

        return ResponseEntity.ok("File uploaded and job started successfully. Job ID:" + jobId);
    }

    @Override
    public ResponseEntity<?> uploadLoan(MultipartFile file) {
        if (!uploadService.validateFileFormat(file)) {
            return ResponseEntity.badRequest().body("Invalid file format. Only CSV files are allowed.");
        }
        if (!uploadService.validateFileSize(file)) {
            return ResponseEntity.badRequest().body("File size exceeds the maximum limit of 50MB.");
        }


        String jobId = uploadService.startLoanJob(file);

        return ResponseEntity.ok("File uploaded and job started successfully. Job ID:" + jobId);
    }

    @Override
    public ResponseEntity<String> downloadDuplicates(@RequestParam String jobId) throws IOException {
            String fileName = "duplicate_offers_" + jobId + ".csv";
            File file = new File(fileName);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            StringBuilder fileContents = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContents.append(line).append("\n");
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fileContents.toString());
        }
}

