package com.smoothstack.branchservice.controller;

import com.smoothstack.branchservice.dto.ServiceTypeDTO;
import com.smoothstack.branchservice.service.ServiceTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/serviceType")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<List<ServiceTypeDTO>> getServiceType(
            @RequestParam(required = false) Integer serviceId,
            @RequestParam(required = false) String serviceTypeName) {

        List<ServiceTypeDTO> serviceTypes = serviceTypeService.getServiceTypesByParams(serviceId, serviceTypeName);

        return ResponseEntity.ok(serviceTypes);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/sort")
    public ResponseEntity<List<ServiceTypeDTO>> getAllServiceTypesSorted(
            @RequestParam(name = "sortBy", defaultValue = "branchId") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {

        List<ServiceTypeDTO> sortedServiceTypes = serviceTypeService.getAllServiceTypesSorted(sortBy, sortOrder);

        return ResponseEntity.ok(sortedServiceTypes);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Page<ServiceTypeDTO>> getFilteredServiceTypes(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) String serviceTypeName,
            Pageable pageable) {
        Page<ServiceTypeDTO> serviceTypes = serviceTypeService.getFilteredServiceTypes(
                branchId, serviceTypeName, pageable);
        return ResponseEntity.ok(serviceTypes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ServiceTypeDTO> createServiceType(@Valid @RequestBody ServiceTypeDTO serviceTypeDTO) {
        ServiceTypeDTO createdServiceType = serviceTypeService.createServiceType(serviceTypeDTO);
        return new ResponseEntity<>(createdServiceType, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{serviceTypeId}")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(
            @PathVariable("serviceTypeId") Integer serviceTypeId,
            @Valid @RequestBody ServiceTypeDTO updatedServiceType
    ) {
        ServiceTypeDTO updatedServiceTypeDTO = serviceTypeService.updateServiceType(serviceTypeId, updatedServiceType);
        return ResponseEntity.ok(updatedServiceTypeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{serviceTypeId}")
    public ResponseEntity<Void> deleteServiceTypeById(@PathVariable Integer serviceTypeId) {
        serviceTypeService.deleteServiceTypeById(serviceTypeId);
        return ResponseEntity.noContent().build();
    }
}