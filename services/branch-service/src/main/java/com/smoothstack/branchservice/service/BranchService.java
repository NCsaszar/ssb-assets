package com.smoothstack.branchservice.service;

import com.smoothstack.branchservice.dto.BranchDTO;
import com.smoothstack.branchservice.exception.custom.BranchNotFoundException;
import com.smoothstack.branchservice.exception.custom.BranchServiceException;
import com.smoothstack.branchservice.exception.PessimisticLockingException;
import com.smoothstack.branchservice.mapper.BranchMapper;
import com.smoothstack.branchservice.model.Branch;
import com.smoothstack.branchservice.model.ServiceType;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.dao.ServiceTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BranchService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final BranchMapper branchMapper;
    private final BranchRepository branchRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    /**
     * Retrieves a list of branches as BranchDTO objects based on the specified parameters.
     *
     * <p>This method allows fetching branches either by a specific branch ID or retrieving all branches.
     * If a branch ID is provided, it calls {@link #getBranchById(Integer)} to retrieve the corresponding branch.
     * If no branch ID is provided (null), it calls {@link #getAllBranches()} to retrieve all branches.
     *
     * @param branchId The ID of the branch for which to retrieve information.
     *                 If null, retrieves all branches.
     * @return A list of BranchDTO objects representing branches based on the specified parameters.
     */
    public List<BranchDTO> getBranchesByParams(Integer branchId) {
        if (branchId != null) {
            return getBranchById(branchId);
        } else {
            return getAllBranches();
        }
    }

    /**
     * Retrieves a list of all branches as BranchDTO objects.
     *
     * <p>This method queries the repository for all branches, maps the resulting
     * entities to BranchDTO objects, and returns a list of BranchDTOs representing all branches.
     *
     * @return A list of BranchDTO objects representing all branches.
     *
     * @throws DataAccessException If there is an issue accessing the data store during the branch retrieval.
     *   Check the exception message for details on the cause of the data access issue.
     *   Additionally, relevant information about the failed branch retrieval attempt is logged.
     *
     * @throws BranchServiceException If an error occurs during the branch retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed branch retrieval attempt is logged.
     */
    public List<BranchDTO> getAllBranches() {
        try {
            logger.info("Fetching all branches.");
            return branchRepository.findAll()
                    .stream()
                    .map(branchMapper::branchToBranchDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Error fetching all branches: {}", ex.getMessage(), ex);
            throw new BranchServiceException("Error fetching all branches. Check the input data and try again", ex) {
            };
        }
    }


    /**
     * Retrieves a list of BranchDTO objects associated with the specified branch ID.
     *
     * <p>This method queries the repository for branches with the given ID, maps the resulting
     * entities to BranchDTO objects, and returns a list of BranchDTOs.
     *
     * @param branchId The ID of the branch for which to retrieve information.
     * @return A list of BranchDTO objects representing branches with the specified ID.
     * @throws BranchServiceException If an error occurs during the branch retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed branch retrieval attempt is logged.
     *
     * @throws DataAccessException If there is an issue accessing the data store during the branch retrieval.
     *   Check the exception message for details on the cause of the data access issue.
     *   Additionally, relevant information about the failed branch retrieval attempt is logged.
     */
    public List<BranchDTO> getBranchById(Integer branchId) {
        try {
            logger.info("Fetching branch with ID: {}", branchId);
            return branchRepository.findByBranchId(branchId)
                    .stream()
                    .map(branchMapper::branchToBranchDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Data access issue while fetching branch: {}", ex.getMessage(), ex);
            throw new BranchServiceException("Failed to fetch branch due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error fetching branch with ID {}: {}", branchId, ex.getMessage(), ex);
            throw new BranchServiceException("Failed to fetch branch. Check the input data and try again.", ex);
        }
    }

    /**
     * Retrieves a list of all branches sorted based on the specified criteria.
     *
     * <p>This method takes two parameters, sortBy and sortOrder, to determine the sorting order of the branches.
     * The branches can be sorted by either "branchName" or "branchCode" in ascending (ASC) or descending (DESC) order.
     *
     * @param sortBy     The field by which the branches should be sorted. Accepted values: "branchName", "branchCode".
     * @param sortOrder  The sorting order for the branches. Accepted values: "ASC" (ascending), "DESC" (descending).
     * @return A sorted list of BranchDTO objects representing branches.
     *
     * @throws BranchServiceException If an error occurs while fetching and sorting branches.
     */
    public List<BranchDTO> getAllBranchesSorted(String sortBy, String sortOrder) {
        try {
            logger.info("Sorting Branches by: {}, in order: {}. Timestamp: {}", sortBy, sortOrder, LocalDateTime.now());  // use timestamp on log
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

            if ("branchName".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "branchName"));
            } else if ("branchCode".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "branchCode"));
            }

            return branchRepository.findAll(sort)
                    .stream()
                    .map(branchMapper::branchToBranchDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error fetching all sorted branches: {}", ex.getMessage(), ex);
            throw new BranchServiceException("Error fetching all sorted branches. Check the input data and try again", ex);
        }
    }

    /**
     * Retrieves a paginated list of BranchDTO objects based on the provided filtering criteria.
     *
     * <p>This method queries the repository for branches that match the specified branchName and branchCode,
     * applies pagination using the provided Pageable object, and maps the resulting entities to BranchDTO objects.
     *
     * @param branchName The name of the branch to filter by. If null or empty, the filter is not applied based on branchName.
     * @param branchCode The code of the branch to filter by. If null or empty, the filter is not applied based on branchCode.
     * @param pageable   The Pageable object specifying the pagination information, including page number, size, and sorting.
     * @return A Page object containing a subset of BranchDTO objects representing branches that match the filtering criteria.
     */
    public Page<BranchDTO> getFilteredBranches(
            String branchName, String branchCode, String address1, String city, String postalCode, Pageable pageable) {

        return branchRepository.findByBranchNameAndBranchCode(
                        branchName, branchCode, address1, city, postalCode, pageable)
                .map(branchMapper::branchToBranchDTO);
    }


    /**
     * Creates a new branch based on the provided BranchDTO.
     *
     * <p>This method transforms the input BranchDTO into a Branch entity, saves it to the repository,
     * and returns the corresponding BranchDTO of the newly created branch.
     *
     * @param branchDTO The BranchDTO containing information for the new branch.
     * @return The BranchDTO representing the newly created branch.
     * @throws BranchServiceException If an error occurs during the branch creation process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed branch creation attempt is logged.
     *
     * @throws DataAccessException If there is an issue accessing the data store during the branch creation.
     *   Check the exception message for details on the cause of the data access issue.
     *   Additionally, relevant information about the failed branch creation attempt is logged.
     */
    @Transactional
    public BranchDTO createBranch(BranchDTO branchDTO) {
        try {
            logger.info("Creating new branch.");
            Branch branch = branchMapper.branchDTOToBranch(branchDTO);
            logger.info("Branch to save: {}", branch);
            Branch savedBranch = branchRepository.save(branch);
            logger.info("Saving branch: {}", savedBranch);
            BranchDTO savedBranchDTO = branchMapper.branchToBranchDTO(savedBranch);
            logger.info(String.valueOf(branchDTO));
            logger.info("Branch created successfully: {}", savedBranchDTO);
            return savedBranchDTO;
        } catch (DataAccessException ex) {
            logger.error("Data access issue while creating branch: {}", ex.getMessage(), ex);
            throw new BranchServiceException("Failed to create branch due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error creating branch: {}", ex.getMessage(), ex);
            logger.error("Failed to create branch with data: {}", branchDTO);
            throw new BranchServiceException("Failed to create branch. Check the input data and try again.", ex);
        }
    }


    /**
     * Adds a ServiceType to a Branch identified by their respective IDs.
     *
     * <p>This method first logs the attempt to add a ServiceType to a Branch with the provided IDs.
     * It then retrieves the Branch and ServiceType entities based on the provided IDs.
     * If the Branch or ServiceType is not found, a {@link EntityNotFoundException} is thrown.
     * Otherwise, the ServiceType is added to the Branch, and the changes are saved to the repository.
     * Success and failure of the addition are logged accordingly.
     *
     * @param branchId The ID of the Branch to which the ServiceType will be added.
     * @param serviceId The ID of the ServiceType to be added to the Branch.
     * @throws EntityNotFoundException If the Branch or ServiceType with the specified IDs is not found.
     *   Check the exception message for details on the missing entity.
     *   Additionally, relevant information about the failed addition attempt is logged.
     *
     * @throws BranchServiceException If an error occurs during the ServiceType addition process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed addition attempt is logged.
     *
     * @throws DataAccessException If there is an issue accessing the data store during the addition.
     *   Check the exception message for details on the cause of the data access issue.
     *   Additionally, relevant information about the failed addition attempt is logged.
     */
    @Transactional
    public void addServiceTypeToBranch(Integer branchId, Integer serviceId) {

        try {
            logger.info("Adding ServiceType to Branch. Branch ID: {}, ServiceType ID: {}", branchId, serviceId);
            Branch branch = branchRepository.findByBranchId(branchId)
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchId));

            ServiceType serviceType = serviceTypeRepository.findByServiceId(serviceId)
                    .orElseThrow(() -> new EntityNotFoundException("ServiceType not found with ID: " + serviceId));

            branch.getServiceTypes().add(serviceType);
            serviceType.getBranches().add(branch);
            branchRepository.save(branch);
            logger.info("ServiceType added to Branch successfully. Branch ID: {}, ServiceType ID: {}", branchId, serviceId);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found issue while adding ServiceType to Branch: {}", ex.getMessage(), ex);
            throw new BranchServiceException("Failed to add ServiceType to Branch due to an entity not found issue. Check the input data and try again.", ex);
        } catch (Exception ex) {
            logger.error("Failed to add ServiceType to Branch. {}", ex.getMessage(), ex);
            throw new BranchServiceException("Failed to add ServiceType to Branch. Check the input data and try again.", ex);
        }
    }


    /**
     * Updates a branch identified by its ID with the provided information.
     *
     * <p>This method retrieves the existing branch based on the specified branch ID,
     * acquires a pessimistic write lock on the entity, updates its properties with the information
     * from the provided {@code updatedBranchDTO}, and saves the changes to the repository.
     *
     * @param branchId The ID of the branch to be updated.
     * @param updatedBranchDTO The BranchDTO containing updated information for the branch.
     * @return The BranchDTO representing the updated branch.
     * @throws EntityNotFoundException If the branch with the specified ID is not found.
     *   Check the exception message for details on the missing branch.
     *   Additionally, relevant information about the failed branch update attempt is logged.
     *
     * @throws PessimisticLockingException If an error occurs while acquiring a pessimistic lock on the existing branch.
     *   Check the exception message for details on the cause of the locking issue.
     *   Additionally, relevant information about the failed lock acquisition attempt is logged.
     *
     * @throws BranchServiceException If an error occurs during the branch update process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed branch update attempt is logged.
     *
     * @throws DataAccessException If there is an issue accessing the data store during the branch update.
     *   Check the exception message for details on the cause of the data access issue.
     *   Additionally, relevant information about the failed branch update attempt is logged.
     */
    @Transactional
    public BranchDTO updateBranch(Integer branchId, BranchDTO updatedBranchDTO) {
        try {
            logger.info("Updating branch with ID: {}", branchId);
            Branch existingBranch = getExistingBranch(branchId);
            Branch updatedBranch = branchMapper.branchDTOToBranch(updatedBranchDTO);
            updateExistingBranch(existingBranch, updatedBranch);
            Branch savedBranch = branchRepository.save(existingBranch);
            logger.info("Branch with ID {} updated successfully", branchId);
            return branchMapper.branchToBranchDTO(savedBranch);
        } catch (EntityNotFoundException ex) {
            logger.error("Error updating branch with ID {}: Branch not found", branchId);
            throw new BranchServiceException("Failed to update Branch due to an entity not found issue. Check the input data and try again.", ex);
        } catch (Exception ex) {
            logger.error("Error updating branch with ID {}: {}", branchId, ex.getMessage(), ex);
            throw new BranchServiceException("Failed to update branch. Check the input data and try again.", ex);
        }
    }


    /**
     * Retrieves the existing branch based on the specified branch ID
     * and acquires a pessimistic write lock on the entity.
     *
     * @param branchId The ID of the branch to be retrieved.
     * @return The existing Branch entity with a pessimistic write lock.
     * @throws EntityNotFoundException If the branch with the specified ID is not found.
     *   Check the exception message for details on the missing branch.
     *   Additionally, relevant information about the failed branch retrieval attempt is logged.
     *
     * @throws PessimisticLockingException If an error occurs while acquiring a pessimistic lock on the existing branch.
     *   Check the exception message for details on the cause of the locking issue.
     *   Additionally, relevant information about the failed lock acquisition attempt is logged.
     */
    private Branch getExistingBranch(Integer branchId) {
        Optional<Branch> optionalBranch = branchRepository.findByBranchId(branchId);
        if (optionalBranch.isPresent()) {
            Branch existingBranch = optionalBranch.get();
            try {
                entityManager.lock(existingBranch, LockModeType.PESSIMISTIC_WRITE);
                return existingBranch;
            } catch (Exception ex) {
                logger.error("Error acquiring pessimistic lock for branch with ID {}: {}", branchId, ex.getMessage(), ex);
                throw new PessimisticLockingException("Failed to acquire pessimistic lock for branch with ID: " + branchId, ex);
            }
        } else {
            throw new EntityNotFoundException("Branch not found with ID: " + branchId);
        }
    }


    /**
     * Updates the existing branch with information from the updated branch.
     *
     * @param existingBranch The existing Branch entity to be updated.
     * @param updatedBranch The Branch entity containing updated information.
     */
    private void updateExistingBranch(Branch existingBranch, Branch updatedBranch) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(updatedBranch, existingBranch);
    }


    /**
     * Deletes a branch by its ID.
     *
     * <p>This method attempts to delete a branch from the repository based on the provided branch ID.
     * If the branch is not found (EmptyResultDataAccessException), a {@link BranchNotFoundException} is thrown.
     * If an unexpected error occurs during the deletion process, a {@link BranchServiceException} is thrown.
     *
     * @param branchId The ID of the branch to be deleted.
     * @throws BranchNotFoundException If the branch with the specified ID is not found.
     *   Check the exception message for details on the missing branch.
     *   Additionally, relevant information about the failed branch deletion attempt is logged.
     *
     * @throws BranchServiceException If an error occurs during the branch deletion process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed branch deletion attempt is logged.
     *
     * @throws DataAccessException If there is an issue accessing the data store during the branch deletion.
     *   Check the exception message for details on the cause of the data access issue.
     *   Additionally, relevant information about the failed branch deletion attempt is logged.
     */
    @Transactional
    public void deleteBranchById(Integer branchId) {
        try {
            logger.info("Deleting branch with ID: {}", branchId);
            Optional<Branch> branchOptional = branchRepository.findById(branchId);
            if (branchOptional.isPresent()) {
                branchRepository.deleteById(branchId);
                logger.info("Branch with ID {} deleted successfully", branchId);
            }
            else {
                throw new BranchNotFoundException("Branch not found with ID: " + branchId);
            }
        } catch (Exception ex) {
            logger.error("Error deleting branch with ID {}: {}", branchId, ex.getMessage(), ex);
            throw new BranchServiceException("Failed to delete branch. Check the input data and try again.", ex);
        }
    }
}