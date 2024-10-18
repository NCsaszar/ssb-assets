package com.smoothstack.branchservice.controllerTest;

import com.smoothstack.branchservice.controller.AppointmentController;
import com.smoothstack.branchservice.dto.AppointmentDTO;
import com.smoothstack.branchservice.dao.AppointmentRepository;
import com.smoothstack.branchservice.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentController appointmentController;

    @Test
    void getAppointmentsShouldReturnListOfAppointments() {
        // Arrange
        Integer appointmentId = 1;
        Integer branchId = 2;
        Integer userId = 3;
        Integer bankerId = 4;

        List<AppointmentDTO> mockAppointments = Collections.singletonList(new AppointmentDTO());
        when(appointmentService.getAppointmentsByParams(eq(appointmentId), eq(branchId), eq(userId), eq(bankerId)))
                .thenReturn(mockAppointments);

        // Act
        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointments(
                appointmentId, branchId, userId, bankerId
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAppointments, response.getBody());
    }

    @Test
    void getAppointmentsShouldReturnEmptyListWhenServiceReturnsEmptyList() {
        // Arrange
        when(appointmentService.getAppointmentsByParams(eq(1), eq(2), eq(3), eq(4)))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointments(1, 2, 3, 4);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void createAppointment_WithValidAppointmentDTO_ShouldReturnCreated() {
        // Arrange
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setBranchId(1);
        when(appointmentService.createAppointment(appointmentDTO)).thenReturn(appointmentDTO);

        // Act
        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(appointmentDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
        verify(appointmentService).createAppointment(appointmentDTO);
    }

    @Test
    void deleteAppointmentById_WithValidId_ShouldReturnNoContent() {
        // Arrange
        int appointmentId = 1;
        doNothing().when(appointmentService).deleteAppointmentById(appointmentId);

        // Act
        ResponseEntity<Void> response = appointmentController.deleteAppointmentById(appointmentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService).deleteAppointmentById(appointmentId);
    }

}