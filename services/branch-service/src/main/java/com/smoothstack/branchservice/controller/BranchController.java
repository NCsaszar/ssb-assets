package com.smoothstack.branchservice.controller;

import com.smoothstack.branchservice.dto.BranchDTO;
import com.smoothstack.branchservice.service.BranchService;
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
@RequestMapping("/api/v1/branch")
public class BranchController {
    private final BranchService branchService;

    /**
     * Retrieves a list of branches based on the specified parameters.
     *
     * <p>This endpoint is restricted to users with the 'ADMIN' or 'CUSTOMER' role, as defined by the {@code @PreAuthorize} annotation.
     * It allows fetching branches either by a specific branch ID or retrieving all branches.
     * If a branch ID is provided as a query parameter, it calls the {@link BranchService#getBranchesByParams(Integer)}
     * method to retrieve the corresponding branch. If no branch ID is provided, it retrieves all branches.
     *
     * @param branchId (Optional) The ID of the branch for which to retrieve information.
     *                 If provided, retrieves information for the specified branch; otherwise, retrieves all branches.
     * @return A ResponseEntity containing a list of {@link BranchDTO} objects representing branches based on the specified parameters.
     *         The response has an HTTP status of 200 (OK) if the request is successful.
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BranchDTO>> getBranches(
            @RequestParam(required = false) Integer branchId) {

        List<BranchDTO> branches = branchService.getBranchesByParams(branchId);

        return ResponseEntity.ok(branches);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/sort")
    public ResponseEntity<List<BranchDTO>> getAllBranchesSorted(
            @RequestParam(name = "sortBy", defaultValue = "branchCode") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {

        List<BranchDTO> sortedBranches = branchService.getAllBranchesSorted(sortBy, sortOrder);

        return ResponseEntity.ok(sortedBranches);
    }


    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Page<BranchDTO>> getFilteredBranches(
            @RequestParam(required = false) String branchName,
            @RequestParam(required = false) String branchCode,
            @RequestParam(required = false) String address1,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String postalCode,

            Pageable pageable) {
        Page<BranchDTO> branches = branchService.getFilteredBranches(
                branchName, branchCode, address1, city, postalCode, pageable);
        return ResponseEntity.ok(branches);
    }

    /**
     * Creates a new branch based on the provided BranchDTO.
     *
     * <p>This endpoint is restricted to users with the 'ADMIN' role, as defined by the {@code @PreAuthorize} annotation.
     * It accepts a POST request with a valid {@link BranchDTO} object representing the information for the new branch.
     * The provided branch information is transformed into a Branch entity, saved to the repository,
     * and the corresponding {@link BranchDTO} of the newly created branch is returned in the response.
     *
     * @param branchDTO The BranchDTO containing information for the new branch. Must be a valid and non-null object.
     * @return A ResponseEntity containing the BranchDTO representing the newly created branch.
     *         The response has an HTTP status of 201 (Created) if the branch is successfully created.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(    // pass only necessary parameters (Integer branchId, Integer bankerId, ...)
            @Valid @RequestBody BranchDTO branchDTO
    ) {
        BranchDTO createdBranch = branchService.createBranch(branchDTO);

        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    /**
     * Adds a ServiceType to a Branch based on the provided branch ID and service type ID.
     *
     * <p>This method is accessible only to users with the 'ADMIN' role, as specified by the {@code PreAuthorize} annotation.
     * It calls the {@link BranchService#addServiceTypeToBranch(Integer, Integer)} method to associate the specified
     * ServiceType with the specified Branch.
     *
     * @param branchId The ID of the Branch to which the ServiceType will be added.
     * @param serviceTypeId The ID of the ServiceType to be added to the Branch.
     * @return A ResponseEntity with a success message if the addition is successful.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/{branchId}/service-type/{serviceTypeId}")
    public ResponseEntity<String> addServiceTypeToBranch(
            @PathVariable Integer branchId,
            @PathVariable Integer serviceTypeId) {

        branchService.addServiceTypeToBranch(branchId, serviceTypeId);

        return ResponseEntity.ok("ServiceType added to Branch successfully");
    }

    /**
     * Updates information for a specific Branch identified by the provided branch ID.
     *
     * <p>This method is accessible only to users with the 'ADMIN' role, as specified by the {@code PreAuthorize} annotation.
     * It calls the {@link BranchService#updateBranch(Integer, BranchDTO)} method to update the specified Branch
     * with the information provided in the {@code updatedBranch} parameter.
     *
     * @param branchId The ID of the Branch to be updated.
     * @param updatedBranch The BranchDTO containing updated information for the Branch.
     * @return A ResponseEntity with the updated BranchDTO if the update is successful.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{branchId}")
    public ResponseEntity<BranchDTO> updateBranch(
            @PathVariable("branchId") Integer branchId,
            @Valid @RequestBody BranchDTO updatedBranch
    ) {
        BranchDTO updatedBranchDTO = branchService.updateBranch(branchId, updatedBranch);

        return ResponseEntity.ok(updatedBranchDTO);
    }

    /**
     * Deletes a specific Branch based on the provided branch ID.
     *
     * <p>This method is accessible only to users with the 'ADMIN' role, as specified by the {@code PreAuthorize} annotation.
     * It calls the {@link BranchService#deleteBranchById(Integer)} method to delete the Branch with the specified ID.
     *
     * @param branchId The ID of the Branch to be deleted.
     * @return A ResponseEntity with no content if the deletion is successful.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{branchId}")
    public ResponseEntity<Void> deleteBranchById(@PathVariable Integer branchId) {
        branchService.deleteBranchById(branchId);

        return ResponseEntity.noContent().build();
    }
}