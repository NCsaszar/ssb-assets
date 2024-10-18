package com.smoothstack.branchservice.controller;

import com.smoothstack.branchservice.dto.BankerDTO;
import com.smoothstack.branchservice.service.BankerService;
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
@RequestMapping("/api/v1/banker")
public class BankerController {
    private final BankerService bankerService;

    /**
     * Retrieves a list of bankers based on specified parameters.
     *
     * This endpoint requires the user to have the 'CUSTOMER' or 'ADMIN' role.
     *
     * @param bankerId (optional) The ID of the banker to retrieve.
     * @param branchId (optional) The ID of the branch associated with the bankers.
     * @return ResponseEntity with a list of BankerDTO objects representing the retrieved bankers.
     *         Returns HttpStatus.OK on success with the list of bankers in the response body.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<List<BankerDTO>> getBankers(
            @RequestParam(required = false) Integer bankerId,
            @RequestParam(required = false) Integer branchId) {

        List<BankerDTO> bankers = bankerService.getBankersByParams(bankerId, branchId);

        return ResponseEntity.ok(bankers);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/sort")
    public ResponseEntity<List<BankerDTO>> getAllBankersSorted(
            @RequestParam(name = "sortBy", defaultValue = "branchId") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {

        List<BankerDTO> sortedBankers = bankerService.getAllBankersSorted(sortBy, sortOrder);

        return ResponseEntity.ok(sortedBankers);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Page<BankerDTO>> getFilteredBankers(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String jobTitle,
            Pageable pageable) {
        Page<BankerDTO> bankers = bankerService.getFilteredBankers(
                branchId, firstName, lastName, jobTitle, pageable);
        return ResponseEntity.ok(bankers);
    }

    /**
     * Creates a new banker based on the provided BankerDTO.
     *
     * This endpoint requires the user to have the 'ADMIN' role.
     *
     * @param bankerDTO The BankerDTO containing information for the new banker.
     *                  Should be a valid and properly formatted JSON in the request body.
     * @return ResponseEntity with the created BankerDTO representing the newly created banker.
     *         Returns HttpStatus.CREATED on success with the created banker in the response body.
     *         Returns HttpStatus.BAD_REQUEST if the provided BankerDTO is invalid.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<BankerDTO> createBanker(@Valid @RequestBody BankerDTO bankerDTO) {
        BankerDTO createdBanker = bankerService.createBanker(bankerDTO);

        return new ResponseEntity<>(createdBanker, HttpStatus.CREATED);
    }

    /**
     * Updates an existing banker with the provided details.
     *
     * This endpoint requires the user to have the 'ADMIN' role.
     *
     * @param bankerId The ID of the banker to be updated, specified in the path.
     * @param updatedBanker The BankerDTO containing the updated information for the banker.
     *                      Should be a valid and properly formatted JSON in the request body.
     * @return ResponseEntity with the updated BankerDTO representing the modified banker.
     *         Returns HttpStatus.OK on success with the updated banker in the response body.
     *         Returns HttpStatus.BAD_REQUEST if the provided BankerDTO is invalid.
     *         Returns HttpStatus.NOT_FOUND if the banker with the given ID is not found.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{bankerId}")
    public ResponseEntity<BankerDTO> updateBanker(
            @PathVariable("bankerId") Integer bankerId,
            @Valid @RequestBody BankerDTO updatedBanker
    ) {
        BankerDTO updatedBankerDTO = bankerService.updateBanker(bankerId, updatedBanker);

        return ResponseEntity.ok(updatedBankerDTO);
    }

    /**
     * Deletes a banker by their ID.
     *
     * This endpoint requires the user to have the 'ADMIN' role.
     *
     * @param bankerId The ID of the banker to be deleted, specified in the path.
     * @return ResponseEntity indicating the result of the delete operation.
     *         Returns HttpStatus.NO_CONTENT on successful deletion.
     *         Returns HttpStatus.NOT_FOUND if the banker with the given ID is not found.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{bankerId}")
    public ResponseEntity<Void> deleteBankerById(@PathVariable Integer bankerId) {
        bankerService.deleteBankerById(bankerId);

        return ResponseEntity.noContent().build();
    }
}
