package com.smoothstack.branchservice.controller;

import com.smoothstack.branchservice.dto.AppointmentDTO;
import com.smoothstack.branchservice.security.JwtService;
import com.smoothstack.branchservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private JwtService jwtService;


    /**
     * Retrieves a list of appointments based on specified parameters.
     *
     * <p>This endpoint requires the user to have the 'CUSTOMER' or 'ADMIN' role.
     *
     * @param appointmentId (optional) The ID of the appointment to retrieve.
     * @param branchId (optional) The ID of the branch associated with the appointments.
     * @param userId (optional) The ID of the user for whom appointments are to be retrieved.
     * @param bankerId (optional) The ID of the banker associated with the appointments.
     * @return ResponseEntity with a list of AppointmentDTO objects representing the retrieved appointments.
     *         Returns HttpStatus.OK on success with the list of appointments in the response body.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<List<AppointmentDTO>> getAppointments(
            @RequestParam(required = false) Integer appointmentId,
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer bankerId) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByParams(appointmentId, branchId, userId, bankerId);

        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/sort")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsSorted(
            @RequestParam(name = "sortBy", defaultValue = "branchId") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {

        List<AppointmentDTO> sortedAppointments = appointmentService.getAllAppointmentsSorted(sortBy, sortOrder);

        return ResponseEntity.ok(sortedAppointments);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/filter-all")
    public ResponseEntity<Page<AppointmentDTO>> getFilteredAppointments(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) Integer bankerId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) LocalDateTime timeslot,
            Pageable pageable) {


        Page<AppointmentDTO> appointments = appointmentService.getFilteredAppointments(
                branchId, bankerId, userId, timeslot, pageable);
        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/filter-banker-timeslot")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsFilteredByBankerIdAndTimeslot(
            @RequestParam(required = false) Integer bankerId,
            @RequestParam(required = false) LocalDateTime timeslot) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsFilteredByBankerIdAndTimeslot(
                bankerId, timeslot);
        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/filter-branch-timeslot")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsFilteredByBranchIdAndTimeslot(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) LocalDateTime timeslot) {

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsFilteredByBranchIdAndTimeslot(
                branchId, timeslot);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Creates a new appointment based on the provided AppointmentDTO.
     *
     * This endpoint requires the user to have the 'CUSTOMER' or 'ADMIN' role.
     *
     * @param appointmentDTO The AppointmentDTO containing information for the new appointment.
     *                       Should be a valid and properly formatted JSON in the request body.
     * @return ResponseEntity with the created AppointmentDTO representing the newly created appointment.
     *         Returns HttpStatus.CREATED on success with the created appointment in the response body.
     *         Returns HttpStatus.BAD_REQUEST if the provided AppointmentDTO is invalid.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {

        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);

        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    /**
     * Updates an existing appointment with the provided details.
     *
     * This endpoint requires the user to have the 'CUSTOMER' or 'ADMIN' role.
     *
     * @param appointmentId The ID of the appointment to be updated, specified in the path.
     * @param updatedAppointment The AppointmentDTO containing the updated information for the appointment.
     *                           Should be a valid and properly formatted JSON in the request body.
     * @return ResponseEntity with the updated AppointmentDTO representing the modified appointment.
     *         Returns HttpStatus.OK on success with the updated appointment in the response body.
     *         Returns HttpStatus.BAD_REQUEST if the provided AppointmentDTO is invalid.
     *         Returns HttpStatus.NOT_FOUND if the appointment with the given ID is not found.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable("appointmentId") Integer appointmentId,
            @Valid @RequestBody AppointmentDTO updatedAppointment
    ) {
        AppointmentDTO updatedAppointmentDTO = appointmentService.updateAppointment(appointmentId, updatedAppointment);

        return ResponseEntity.ok(updatedAppointmentDTO);
    }

    /**
     * Deletes an appointment by its ID.
     *
     * This endpoint requires the user to have the 'CUSTOMER' or 'ADMIN' role.
     *
     * @param appointmentId The ID of the appointment to be deleted, specified in the path.
     * @return ResponseEntity indicating the result of the delete operation.
     *         Returns HttpStatus.NO_CONTENT on successful deletion.
     *         Returns HttpStatus.NOT_FOUND if the appointment with the given ID is not found.
     *         Returns HttpStatus.FORBIDDEN if the user does not have the required role.
     *         Returns HttpStatus.INTERNAL_SERVER_ERROR if an unexpected error occurs during the operation.
     */
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{appointmentId}")
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable Integer appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);

        return ResponseEntity.noContent().build();
    }
}