package com.smoothstack.branchservice.modelTest;

import com.smoothstack.branchservice.model.Branch;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BranchTest {

    @Test
    void testBranchEntity() {
        // Create a Branch object
        Branch branch = new Branch();
        branch.setBranchCode("ABC123");
        branch.setBranchName("Test Branch");
        branch.setBranchManager(1);
        branch.setPhoneNumber("123-456-7890");
        branch.setEmail("test@test.com");
        branch.setAddress1("123 Main St");
        branch.setAddress2("apt. 3");
        branch.setCity("Anytown");
        branch.setState("Anystate");
        branch.setPostalCode("12345");
        branch.setCountry("Country");
        branch.setLat(12.3456);
        branch.setLng(-78.9012);

        // Set dateCreated and dateModified
        branch.setDateCreated(new Timestamp(System.currentTimeMillis()));
        branch.setDateModified(new Timestamp(System.currentTimeMillis()));

        // Test getters
        assertEquals("ABC123", branch.getBranchCode());
        assertEquals("Test Branch", branch.getBranchName());
        assertEquals(1, branch.getBranchManager());
        assertEquals("123-456-7890", branch.getPhoneNumber());
        assertEquals("test@test.com", branch.getEmail());
        assertEquals("123 Main St", branch.getAddress1());
        assertEquals("apt. 3", branch.getAddress2());
        assertEquals("Anytown", branch.getCity());
        assertEquals("Anystate", branch.getState());
        assertEquals("12345", branch.getPostalCode());
        assertEquals("Country", branch.getCountry());
        assertEquals(12.3456, branch.getLat());
        assertEquals(-78.9012, branch.getLng());

        // Test dateCreated and dateModified
        assertNotNull(branch.getDateCreated());
        assertNotNull(branch.getDateModified());

        // Test initial lists
        assertNotNull(branch.getBankers());
        assertNotNull(branch.getAppointments());
        assertNotNull(branch.getQueues());
        assertNotNull(branch.getServiceTypes());
        assertEquals(0, branch.getBankers().size());
        assertEquals(0, branch.getAppointments().size());
        assertEquals(0, branch.getQueues().size());
        assertEquals(0, branch.getServiceTypes().size());
    }
}
