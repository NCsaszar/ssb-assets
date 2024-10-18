package com.smoothstack.branchservice.daoTest;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.model.Branch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestConfig.class)
public class BranchRepositoryTest {

    @Autowired
    private BranchRepository branchRepository;

    @Test
    public void testFindByBranchId() {
        // Arrange
        Branch branch = new Branch();
        branch.setBranchId(1);
        branchRepository.save(branch);

        // Act
        Optional<Branch> result = branchRepository.findByBranchId(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(branch.getBranchId(), result.get().getBranchId());
    }
}