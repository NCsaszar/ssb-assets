package com.smoothstack.branchservice.controller;

import com.smoothstack.branchservice.dto.QueueDTO;
import com.smoothstack.branchservice.service.QueueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final QueueService queueService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<List<QueueDTO>> getQueues(
            @RequestParam(required = false) Integer queueId,
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) Integer userId) {

        List<QueueDTO> queues = queueService.getQueuesByParams(queueId, branchId, userId);

        return ResponseEntity.ok(queues);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/create-queue")
    //    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QueueDTO> createQueue(@Valid @RequestBody QueueDTO queueDTO) {
        QueueDTO createdQueue = queueService.createQueue(queueDTO);
        return new ResponseEntity<>(createdQueue, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-queue/{queueId}")
    public ResponseEntity<QueueDTO> updateQueue(
            @PathVariable("queueId") Integer queueId,
            @Valid @RequestBody QueueDTO updatedQueue
    ) {
        QueueDTO updatedQueueDTO = queueService.updateQueue(queueId, updatedQueue);
        return ResponseEntity.ok(updatedQueueDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-queue/{queueId}")
    public ResponseEntity<Void> deleteQueueById(@PathVariable Integer queueId) {
        queueService.deleteQueueById(queueId);
        return ResponseEntity.noContent().build();
    }
}