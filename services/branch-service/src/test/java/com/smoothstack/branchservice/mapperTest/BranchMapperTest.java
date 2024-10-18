package com.smoothstack.branchservice.mapperTest;

import com.smoothstack.branchservice.dto.BranchDTO;
import com.smoothstack.branchservice.mapper.BranchMapper;
import com.smoothstack.branchservice.mapper.BranchMapperImpl;
import com.smoothstack.branchservice.model.Branch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BranchMapperTest {


    @InjectMocks
    private BranchMapper branchMapper = new BranchMapperImpl();

    @Test
    void branchDTOToBranch() {
        // Arrange
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchCode("ABC123");
        branchDTO.setBranchName("Test Branch");

        // Act
        Branch branch = branchMapper.branchDTOToBranch(branchDTO);

        // Assert
        assertEquals(branchDTO.getBranchCode(), branch.getBranchCode());
        assertEquals(branchDTO.getBranchName(), branch.getBranchName());
        // Add more assertions for other properties
    }

    @Test
    void branchToBranchDTO() {
        // Arrange
        Branch branch = new Branch();
        branch.setBranchCode("ABC123");
        branch.setBranchName("Test Branch");

        // Act
        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);

        // Assert
        assertEquals(branch.getBranchCode(), branchDTO.getBranchCode());
        assertEquals(branch.getBranchName(), branchDTO.getBranchName());
        // Add more assertions for other properties
    }
}


//    private final BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);
//
//    @Test
//    void branchDTOToBranch() {
//        // Arrange
//        BranchDTO branchDTO = new BranchDTO();
//        branchDTO.setBranchCode("ABC123");
//        branchDTO.setBranchName("Test Branch");
//
//        // Act
//        Branch branch = branchMapper.branchDTOToBranch(branchDTO);
//
//        // Assert
//        assertEquals(branchDTO.getBranchCode(), branch.getBranchCode());
//        assertEquals(branchDTO.getBranchName(), branch.getBranchName());
//    }
//
//    @Test
//    void branchToBranchDTO() {
//        // Arrange
//        Branch branch = new Branch();
//        branch.setBranchCode("ABC123");
//        branch.setBranchName("Test Branch");
//
//        // Act
//        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);
//
//        // Assert
//        assertEquals(branch.getBranchCode(), branchDTO.getBranchCode());
//        assertEquals(branch.getBranchName(), branchDTO.getBranchName());
//    }
//}