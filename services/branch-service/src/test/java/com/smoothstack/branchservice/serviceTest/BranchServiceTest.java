package com.smoothstack.branchservice.serviceTest;

import com.smoothstack.branchservice.dao.ServiceTypeRepository;
import com.smoothstack.branchservice.dto.BranchDTO;
import com.smoothstack.branchservice.exception.custom.BranchNotFoundException;
import com.smoothstack.branchservice.exception.custom.BranchServiceException;
import com.smoothstack.branchservice.mapper.BranchMapper;
import com.smoothstack.branchservice.model.Branch;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.model.ServiceType;
import com.smoothstack.branchservice.service.BranchService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class BranchServiceTest {

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BranchMapper branchMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BranchService branchService;

    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    @Test
    void testGetBranchesByParams_WithBranchId_ReturnsBranchById() {
        // Arrange
        Integer branchId = 1;
        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setBranchName("Branch 1");

        Optional<Branch> branches = Optional.of(branch);
        Mockito.when(branchRepository.findByBranchId(branchId)).thenReturn(branches);

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(branchId);
        branchDTO.setBranchName("Branch 1");

        Mockito.when(branchMapper.branchToBranchDTO(branch)).thenReturn(branchDTO);

        // Act
        List<BranchDTO> result = branchService.getBranchesByParams(branchId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(branchDTO, result.get(0));
        Mockito.verify(branchRepository).findByBranchId(branchId);
        Mockito.verify(branchMapper).branchToBranchDTO(branch);
    }

//    @Test
//    void testGetBranchesByParams_WithNullBranchId_ReturnsAllBranches() {
//        // Arrange
//        BranchDTO branchDTO1 = new BranchDTO();
//        branchDTO1.setBranchId(1);
//        branchDTO1.setBranchName("Branch 1");
//
//        BranchDTO branchDTO2 = new BranchDTO();
//        branchDTO2.setBranchId(2);
//        branchDTO2.setBranchName("Branch 2");
//
//        Mockito.when(branchService.getAllBranches()).thenReturn(List.of(branchDTO1, branchDTO2));
//
//        // Act
//        List<BranchDTO> result = branchService.getBranchesByParams(null);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals(branchDTO1, result.get(0));
//        assertEquals(branchDTO2, result.get(1));
//        Mockito.verify(branchService).getAllBranches();
//        Mockito.verifyNoInteractions(branchService, branchRepository, branchMapper);
//    }

    @Test
    public void testGetBranchesByParams_NullBranchId() {
        // Arrange
        Integer branchId = null;
//        when(branchRepository.findAll()).thenReturn(Arrays.asList(/* mock your branch entities here */));
        Mockito.when(branchRepository.findAll()).thenReturn(Arrays.asList(/* mock your branch entities here */));

        // Act
        List<BranchDTO> branches = branchService.getBranchesByParams(branchId);

        // Assert
        verify(branchRepository, times(1)).findAll();
        // Add more assertions to verify the behavior of your method based on the mock data returned
    }

    @Test
    public void testGetAllBranches_Success() {
        // Arrange
        Branch branch1 = new Branch();
        branch1.setBranchId(1);
        branch1.setBranchCode("ABC123");
        branch1.setBranchName("branch name 1");
        branch1.setBranchManager(111);
        branch1.setPhoneNumber("123-456-7890");
        branch1.setEmail("test@test.com");
        branch1.setAddress1("123 Main St");
        branch1.setAddress2("apt. 3");
        branch1.setCity("Anytown");
        branch1.setState("Anystate");
        branch1.setPostalCode("12345");
        branch1.setCountry("Country");
        branch1.setLat(12.3456);
        branch1.setLng(-78.9012);
        branch1.setDateCreated(new Timestamp(System.currentTimeMillis()));
        branch1.setDateModified(new Timestamp(System.currentTimeMillis()));

        Branch branch2 = new Branch();
        branch2.setBranchId(2);
        branch2.setBranchCode("DEF456");
        branch2.setBranchName("branch name 2");
        branch2.setBranchManager(222);
        branch2.setPhoneNumber("012-345-6789");
        branch2.setEmail("test2@test.com");
        branch2.setAddress1("321 Main St");
        branch2.setAddress2("apt. 1");
        branch2.setCity("Someville");
        branch2.setState("Somestate");
        branch2.setPostalCode("54321");
        branch2.setCountry("Country2");
        branch2.setLat(21.3456);
        branch2.setLng(-87.9012);
        branch2.setDateCreated(new Timestamp(System.currentTimeMillis()));
        branch2.setDateModified(new Timestamp(System.currentTimeMillis()));

        List<Branch> branches = Arrays.asList(branch1, branch2);

        Mockito.when(branchRepository.findAll()).thenReturn(branches);

        BranchDTO branchDTO1 = new BranchDTO();
        branchDTO1.setBranchId(1);
        branchDTO1.setBranchCode("ABC123");
        branchDTO1.setBranchName("branch name 1");
        branchDTO1.setBranchManager(111);
        branchDTO1.setPhoneNumber("123-456-7890");
        branchDTO1.setEmail("test@test.com");
        branchDTO1.setAddress1("123 Main St");
        branchDTO1.setAddress2("apt. 3");
        branchDTO1.setCity("Anytown");
        branchDTO1.setState("Anystate");
        branchDTO1.setPostalCode("12345");
        branchDTO1.setCountry("Country");
        branchDTO1.setLat(12.3456);
        branchDTO1.setLng(-78.9012);

        BranchDTO branchDTO2 = new BranchDTO();
        branchDTO2.setBranchId(2);
        branchDTO2.setBranchCode("DEF456");
        branchDTO2.setBranchName("branch name 2");
        branchDTO2.setBranchManager(222);
        branchDTO2.setPhoneNumber("012-345-6789");
        branchDTO2.setEmail("test2@test.com");
        branchDTO2.setAddress1("321 Main St");
        branchDTO2.setAddress2("apt. 1");
        branchDTO2.setCity("Someville");
        branchDTO2.setState("Somestate");
        branchDTO2.setPostalCode("54321");
        branchDTO2.setCountry("Country2");
        branchDTO2.setLat(21.3456);
        branchDTO2.setLng(-87.9012);

        List<BranchDTO> expectedBranchDTOs = Arrays.asList(branchDTO1, branchDTO2);

        Mockito.when(branchMapper.branchToBranchDTO(branch1)).thenReturn(branchDTO1);
        Mockito.when(branchMapper.branchToBranchDTO(branch2)).thenReturn(branchDTO2);

        // Act
        List<BranchDTO> result = branchService.getAllBranches();

        List<Branch> actualBranches = branchRepository.findAll();
        logger.info("Actual branches: {}", actualBranches);
        logger.info("Result: {}", result);

        // Assert
        assertEquals(expectedBranchDTOs.size(), result.size());
        for (int i = 0; i < expectedBranchDTOs.size(); i++) {
            assertEquals(expectedBranchDTOs.get(i), result.get(i));
        }

        verify(branchRepository, times(2)).findAll();
        verify(branchMapper, times(2)).branchToBranchDTO(any(Branch.class));
    }

    @Test
    void testGetBranchById_Success() {
        // Arrange
        Integer branchId = 1;
        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setBranchName("Branch 1");

        Optional<Branch> branchOptional = Optional.of(branch);
        Mockito.when(branchRepository.findByBranchId(branchId)).thenReturn(branchOptional);

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(branchId);
        branchDTO.setBranchName("Branch 1");

        Mockito.when(branchMapper.branchToBranchDTO(branch)).thenReturn(branchDTO);

        // Act
        List<BranchDTO> result = branchService.getBranchById(branchId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(branchDTO, result.get(0));
        Mockito.verify(branchRepository).findByBranchId(branchId);
        Mockito.verify(branchMapper).branchToBranchDTO(branch);
    }

    @Test
    public void testGetBranchById_DataAccessException() {
        // Arrange
        Integer branchId = 1;
//        when(branchRepository.findByBranchId(branchId)).thenThrow(new DataAccessException("Data access exception") {
//        });
        Mockito.when(branchRepository.findByBranchId(branchId)).thenThrow(new DataAccessException("Data access exception") {});

        // Act + Assert
        assertThrows(BranchServiceException.class, () -> {
            branchService.getBranchById(branchId);
        });
    }

    @Test
    public void testGetBranchById_GenericException() {
        // Arrange
        Integer branchId = 1;
//        when(branchRepository.findByBranchId(branchId)).thenThrow(new RuntimeException("Generic exception"));
        Mockito.when(branchRepository.findByBranchId(branchId)).thenThrow(new RuntimeException("Generic exception"));


        // Act + Assert
        assertThrows(BranchServiceException.class, () -> {
            branchService.getBranchById(branchId);
        });
    }

    @Test
    void testGetAllBranchesSorted_Success() {
        // Arrange
        String sortBy = "branchName";
        String sortOrder = "ASC";

        Branch branch1 = new Branch();
        branch1.setBranchId(1);
        branch1.setBranchName("A Test");
        branch1.setBranchCode("SSB001");

        Branch branch2 = new Branch();
        branch2.setBranchId(2);
        branch2.setBranchName("B Test");
        branch2.setBranchCode("SSB002");

        List<Branch> branches = List.of(branch1, branch2);

        BranchDTO branchDTO1 = new BranchDTO();
        branchDTO1.setBranchId(1);
        branchDTO1.setBranchName("A Test");
        branchDTO1.setBranchCode("SSB001");

        BranchDTO branchDTO2 = new BranchDTO();
        branchDTO2.setBranchId(2);
        branchDTO2.setBranchName("B Test");
        branchDTO2.setBranchCode("SSB002");

        Mockito.when(branchRepository.findAll(Mockito.any(Sort.class))).thenReturn(branches);
        Mockito.when(branchMapper.branchToBranchDTO(branch1)).thenReturn(branchDTO1);
        Mockito.when(branchMapper.branchToBranchDTO(branch2)).thenReturn(branchDTO2);

        // Act
        List<BranchDTO> result = branchService.getAllBranchesSorted(sortBy, sortOrder);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(branchDTO2, result.get(1)); // A Test should come first when sorted by branchName ASC
        assertEquals(branchDTO1, result.get(0)); // B Test should come second when sorted by branchName ASC

        Mockito.verify(branchRepository).findAll(Mockito.any(Sort.class));
        Mockito.verify(branchMapper, Mockito.times(2)).branchToBranchDTO(Mockito.any(Branch.class));
        Mockito.verifyNoMoreInteractions(branchRepository, branchMapper);
    }

    @Test
    public void testCreateBranch_Success() {
        // Arrange
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(1);
        branchDTO.setBranchCode("ABC123");
        branchDTO.setBranchName("branch name 1");
        branchDTO.setBranchManager(111);
        branchDTO.setPhoneNumber("123-456-7890");
        branchDTO.setEmail("test@test.com");
        branchDTO.setAddress1("123 Main St");
        branchDTO.setAddress2("apt. 3");
        branchDTO.setCity("Anytown");
        branchDTO.setState("Anystate");
        branchDTO.setPostalCode("12345");
        branchDTO.setCountry("Country");
        branchDTO.setLat(12.3456);
        branchDTO.setLng(-78.9012);

        Branch branch = Branch.builder()
                .branchCode(branchDTO.getBranchCode())
                .branchName(branchDTO.getBranchName())
                .branchManager(branchDTO.getBranchManager())
                .phoneNumber(branchDTO.getPhoneNumber())
                .email(branchDTO.getEmail())
                .address1(branchDTO.getAddress1())
                .address2(branchDTO.getAddress2())
                .city(branchDTO.getCity())
                .state(branchDTO.getState())
                .postalCode(branchDTO.getPostalCode())
                .country(branchDTO.getCountry())
                .lat(branchDTO.getLat())
                .lng(branchDTO.getLng())
                .build();

        lenient().when(branchRepository.findByBranchId(branchDTO.getBranchId())).thenReturn(Optional.empty());
        when(branchMapper.branchDTOToBranch(branchDTO)).thenReturn(branch);
        logger.info("branchDTO: {}", branchDTO);
        logger.info("branch: {}", branch);
        when(branchRepository.save(branch)).thenReturn(branch);
        when(branchMapper.branchToBranchDTO(branch)).thenReturn(branchDTO);

        // Act
        BranchDTO result = branchService.createBranch(branchDTO);
        logger.info("Result: {}", result);

        // Assert
        assertNotNull(result);
//        verify(branchRepository).findByBranchId(branchDTO.getBranchId());
        verify(branchRepository).save(branch);
    }

    @Test
    void testAddServiceTypeToBranch_Success() {
        // Arrange
        Integer branchId = 1;
        Integer serviceId = 1;

        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setBranchName("Branch 1");

        ServiceType serviceType = new ServiceType();
        serviceType.setServiceId(serviceId);
        serviceType.setServiceTypeName("ServiceType 1");

        Mockito.when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.of(branch));
        Mockito.when(serviceTypeRepository.findByServiceId(serviceId)).thenReturn(Optional.of(serviceType));

        // Act
        branchService.addServiceTypeToBranch(branchId, serviceId);

        // Assert
        assertTrue(branch.getServiceTypes().contains(serviceType));
        assertTrue(serviceType.getBranches().contains(branch));
        Mockito.verify(branchRepository).findByBranchId(branchId);
        Mockito.verify(serviceTypeRepository).findByServiceId(serviceId);
        Mockito.verify(branchRepository).save(branch);
        Mockito.verifyNoMoreInteractions(branchRepository, serviceTypeRepository);
    }

    @Test
    public void testUpdateBranch_Success() {
        // Arrange
        Integer branchId = 1;
        BranchDTO updatedBranchDTO = new BranchDTO();
        updatedBranchDTO.setBranchId(branchId);
        updatedBranchDTO.setBranchName("New Branch Name");

        Branch existingBranch = new Branch();
        existingBranch.setBranchId(branchId);
        existingBranch.setBranchName("Old Branch Name");

        Branch updatedBranch = new Branch();
        updatedBranch.setBranchId(branchId);
        updatedBranch.setBranchName("New Branch Name");

        when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.of(existingBranch));
        when(branchMapper.branchDTOToBranch(updatedBranchDTO)).thenReturn(updatedBranch);
        when(branchRepository.save(existingBranch)).thenReturn(updatedBranch);
        when(branchMapper.branchToBranchDTO(updatedBranch)).thenReturn(updatedBranchDTO);

        // Act
        BranchDTO result = branchService.updateBranch(branchId, updatedBranchDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedBranchDTO.getBranchName(), result.getBranchName());
        verify(branchRepository).findByBranchId(branchId);
        verify(branchMapper).branchDTOToBranch(updatedBranchDTO);
        verify(branchRepository).save(existingBranch);
        verify(branchMapper).branchToBranchDTO(updatedBranch);
    }

    @Test
//    public void testUpdateBranch_Failure_InvalidBranchId_ThrowsEntityNotFoundException() {
    public void testUpdateBranch_Failure_InvalidBranchId_ThrowsBranchServiceException() {
        // Arrange
        Integer branchId = 1;
        BranchDTO updatedBranchDTO = new BranchDTO();
        updatedBranchDTO.setBranchId(branchId);
        updatedBranchDTO.setBranchName("New Branch Name");

        when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BranchServiceException.class, () -> {
            branchService.updateBranch(branchId, updatedBranchDTO);
        });
    }

    @Test
    public void testUpdateBranch_Failure_Exception_ThrowsBranchServiceException() {
        // Arrange
        Integer branchId = 1;
        BranchDTO updatedBranchDTO = new BranchDTO();
        updatedBranchDTO.setBranchId(branchId);
        updatedBranchDTO.setBranchName("New Branch Name");

        Branch existingBranch = new Branch();
        existingBranch.setBranchId(branchId);
        existingBranch.setBranchName("Old Branch Name");

        lenient().when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.of(existingBranch));
        lenient().when(branchMapper.branchDTOToBranch(updatedBranchDTO)).thenReturn(new Branch());
        lenient().when(branchRepository.save(existingBranch)).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(BranchServiceException.class, () -> {
            branchService.updateBranch(branchId, updatedBranchDTO);
        });
    }

    @Test
    void testDeleteBranchById_Success() {
        // Arrange
        Integer branchId = 1;
        doNothing().when(branchRepository).deleteById(branchId);
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(new Branch())); // Return a dummy branch for findById

        // Act
        branchService.deleteBranchById(branchId);
    }

    @Test
    void testDeleteBranchById_Failure_InvalidId_ThrowsBranchNotFoundException() {
        // Arrange
        Integer branchId = 1;
        lenient().doThrow(BranchNotFoundException.class).when(branchRepository).deleteById(branchId);

        // Act and Assert
//        assertThrows(BranchNotFoundException.class, () -> branchService.deleteBranchById(branchId));
        assertThrows(BranchServiceException.class, () -> branchService.deleteBranchById(branchId));

    }

    @Test
    void testDeleteBranchById_Failure_Exception_ThrowsBranchServiceException() {
        // Arrange
        Integer branchId = 1;
        lenient().doThrow(RuntimeException.class).when(branchRepository).deleteById(branchId);

        // Act and Assert
        assertThrows(BranchServiceException.class, () -> branchService.deleteBranchById(branchId));

    }
}