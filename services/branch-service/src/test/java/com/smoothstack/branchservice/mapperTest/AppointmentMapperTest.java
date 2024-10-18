package com.smoothstack.branchservice.mapperTest;

import com.smoothstack.branchservice.dto.AppointmentDTO;
import com.smoothstack.branchservice.mapper.AppointmentMapper;
import com.smoothstack.branchservice.mapper.AppointmentMapperImpl;
import com.smoothstack.branchservice.model.Appointment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AppointmentMapperTest {

    @InjectMocks
    private AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    @Test
    void appointmentDTOToAppointment() {
        // Arrange
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setBranchId(1);
        appointmentDTO.setBankerId(2);

        // Act
        Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);

        // Assert
        assertEquals(appointmentDTO.getBranchId(), appointment.getBranchId());
        assertEquals(appointmentDTO.getBankerId(), appointment.getBankerId());
        // Add more assertions for other properties
    }
}
