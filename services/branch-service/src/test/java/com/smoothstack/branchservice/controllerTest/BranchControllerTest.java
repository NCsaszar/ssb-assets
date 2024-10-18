package com.smoothstack.branchservice.controllerTest;

import com.smoothstack.branchservice.controller.BranchController;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.dto.BranchDTO;
import com.smoothstack.branchservice.service.BranchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchController branchController;

    @Test
    void getBranches_WithValidParams_ShouldReturnBranches() {
        // Arrange
        Integer branchId = 1;

        List<BranchDTO> branches = new ArrayList<>();
        branches.add(new BranchDTO());

        when(branchService.getBranchesByParams(branchId)).thenReturn(branches);

        // Act
        ResponseEntity<List<BranchDTO>> response = branchController.getBranches(branchId);

        // Assert
        assertEquals(branches, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(branchService).getBranchesByParams(branchId);
    }

    @Test
    void getAllBranchesSorted_WithValidParams_ShouldReturnSortedBranches() {
        // Arrange
        String sortBy = "branchName";
        String sortOrder = "DESC";

        List<BranchDTO> sortedBranches = new ArrayList<>();
        sortedBranches.add(new BranchDTO());

        when(branchService.getAllBranchesSorted(sortBy, sortOrder)).thenReturn(sortedBranches);

        // Act
        ResponseEntity<List<BranchDTO>> response = branchController.getAllBranchesSorted(sortBy, sortOrder);

        // Assert
        assertEquals(sortedBranches, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(branchService).getAllBranchesSorted(sortBy, sortOrder);
    }

    @Test
    void getFilteredBranches_WithValidParams_ShouldReturnFilteredBranches() {
        // Arrange
        String branchName = "Test Branch";
        String branchCode = "123";
        String address1 = "123 Main St";
        String city = "City";
        String postalCode = "12345";
        Pageable pageable = Pageable.unpaged();

        List<BranchDTO> branchDTOList = new ArrayList<>();
        branchDTOList.add(new BranchDTO());
        Page<BranchDTO> branchDTOPage = new PageImpl<>(branchDTOList);

        when(branchService.getFilteredBranches(branchName, branchCode, address1, city, postalCode, pageable))
                .thenReturn(branchDTOPage);

        // Act
        ResponseEntity<Page<BranchDTO>> response = branchController.getFilteredBranches(branchName, branchCode,
                address1, city, postalCode, pageable);

        // Assert
        assertEquals(branchDTOPage, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(branchService).getFilteredBranches(branchName, branchCode, address1, city, postalCode, pageable);
    }

    @Test
    void createBranch_WithValidBranchDTO_ShouldReturnCreated() {
        // Arrange
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchName("Test Branch");
        when(branchService.createBranch(branchDTO)).thenReturn(branchDTO);

        // Act
        ResponseEntity<BranchDTO> response = branchController.createBranch(branchDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(branchDTO, response.getBody());
        verify(branchService).createBranch(branchDTO);
    }

    @Test
    void addServiceTypeToBranch_WithValidBranchIdAndServiceTypeId_ShouldReturnOk() {
        // Arrange
        int branchId = 1;
        int serviceTypeId = 1;
        doNothing().when(branchService).addServiceTypeToBranch(branchId, serviceTypeId);

        // Act
        ResponseEntity<String> response = branchController.addServiceTypeToBranch(branchId, serviceTypeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ServiceType added to Branch successfully", response.getBody());
        verify(branchService).addServiceTypeToBranch(branchId, serviceTypeId);
    }

    @Test
    void updateBranch_WithValidIdAndBranchDTO_ShouldReturnUpdatedBranchDTO() {
        // Arrange
        int branchId = 1;
        BranchDTO updatedBranchDTO = new BranchDTO(); // Create a sample updated BranchDTO
        when(branchService.updateBranch(branchId, updatedBranchDTO)).thenReturn(updatedBranchDTO);

        // Act
        ResponseEntity<BranchDTO> response = branchController.updateBranch(branchId, updatedBranchDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBranchDTO, response.getBody());
        verify(branchService).updateBranch(branchId, updatedBranchDTO);
    }

    @Test
    void deleteBranchById_WithValidId_ShouldReturnNoContent() {
        // Arrange
        int branchId = 1;
        doNothing().when(branchService).deleteBranchById(branchId);

        // Act
        ResponseEntity<Void> response = branchController.deleteBranchById(branchId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(branchService).deleteBranchById(branchId);
    }
}
