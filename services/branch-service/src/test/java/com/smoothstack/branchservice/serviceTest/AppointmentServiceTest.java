package com.smoothstack.branchservice.serviceTest;

import com.smoothstack.branchservice.dao.AppointmentRepository;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.mapper.AppointmentMapper;
import com.smoothstack.branchservice.service.AppointmentService;
import com.smoothstack.branchservice.service.BankerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
//    @SpringBootTest
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    private static final Logger logger = LoggerFactory.getLogger(BankerService.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteAppointmentById() {
        // Arrange
        Integer appointmentId = 1;
//            lenient().doNothing().when(bankerRepository).deleteById(bankerId);

        // Act
        appointmentService.deleteAppointmentById(appointmentId);
    }
}