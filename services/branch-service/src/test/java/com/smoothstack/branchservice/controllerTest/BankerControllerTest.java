package com.smoothstack.branchservice.controllerTest;

import com.smoothstack.branchservice.controller.BankerController;
import com.smoothstack.branchservice.dao.BankerRepository;
import com.smoothstack.branchservice.dto.BankerDTO;
import com.smoothstack.branchservice.service.BankerService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankerControllerTest {

    @Mock
    private BankerService bankerService;

    @Mock
    private BankerRepository bankerRepository;

    @InjectMocks
    private BankerController bankerController;

    @Test
    void getBankersShouldReturnListOfBankers() {
        // Arrange
        Integer bankerId = 1;
        Integer branchId = 2;

        List<BankerDTO> mockBankers = Collections.singletonList(new BankerDTO());
        when(bankerService.getBankersByParams(eq(bankerId), eq(branchId)))
                .thenReturn(mockBankers);

        // Act
        ResponseEntity<List<BankerDTO>> response = bankerController.getBankers(
                bankerId, branchId
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockBankers, response.getBody());
    }

    @Test
    void createBanker_WithValidBankerDTO_ShouldReturnCreated() {
        // Arrange
        BankerDTO bankerDTO = new BankerDTO();
        bankerDTO.setBranchId(1);
        when(bankerService.createBanker(bankerDTO)).thenReturn(bankerDTO);

        // Act
        ResponseEntity<BankerDTO> response = bankerController.createBanker(bankerDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bankerDTO, response.getBody());
        verify(bankerService).createBanker(bankerDTO);
    }

    @Test
    void deleteBankerById_WithValidId_ShouldReturnNoContent() {
        // Arrange
        int bankerId = 1;
        doNothing().when(bankerService).deleteBankerById(bankerId);

        // Act
        ResponseEntity<Void> response = bankerController.deleteBankerById(bankerId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bankerService).deleteBankerById(bankerId);
    }
}
