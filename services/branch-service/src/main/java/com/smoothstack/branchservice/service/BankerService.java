package com.smoothstack.branchservice.service;

import com.smoothstack.branchservice.dto.BankerDTO;
import com.smoothstack.branchservice.exception.custom.BankerNotFoundException;
import com.smoothstack.branchservice.exception.custom.BankerServiceException;
import com.smoothstack.branchservice.exception.custom.BranchServiceException;
import com.smoothstack.branchservice.mapper.BankerMapper;
import com.smoothstack.branchservice.model.Banker;
import com.smoothstack.branchservice.model.Branch;
import com.smoothstack.branchservice.dao.BankerRepository;
import com.smoothstack.branchservice.dao.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BankerService {

    private final BankerMapper bankerMapper;
    private final BankerRepository bankerRepository;
    private final BranchRepository branchRepository;
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    /**
     * Retrieves a list of BankerDTO objects based on the specified parameters.
     *
     * <p>This method allows fetching bankers based on various criteria such as bankerId or branchId.
     * If both parameters are null, it returns a list of all bankers.
     * If bankerId is provided, it retrieves the specific banker with that ID.
     * If branchId is provided, it retrieves all bankers associated with the specified branch.
     *
     * @param bankerId  The ID of the specific banker to retrieve. If provided, branchId is ignored.
     * @param branchId  The ID of the branch for which to retrieve bankers. Ignored if bankerId is specified.
     * @return A list of BankerDTO objects representing bankers based on the provided parameters.
     */
    public List<BankerDTO> getBankersByParams(Integer bankerId, Integer branchId) {
        if (bankerId != null) {
            return getBankerById(bankerId);
        }
        else if (branchId != null) {
            return getBankersByBranchId(branchId);
        } else {
            return getAllBankers();
        }
    }

    /**
     * Retrieves a list of all BankerDTO objects representing all bankers.
     *
     * <p>This method fetches all bankers from the repository, maps the resulting entities to BankerDTO objects,
     * and returns a list of BankerDTOs.
     *
     * @return A list of BankerDTO objects representing all bankers.
     *
     * @throws BranchServiceException If an error occurs during the banker retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed banker retrieval attempt is logged.
     */
    public List<BankerDTO> getAllBankers() {
        try {
            logger.info("Fetching all bankers.");

            return bankerRepository.findAll()
                    .stream()
                    .map(bankerMapper::bankerToBankerDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Error fetching all appointments: {}", ex.getMessage(), ex);
            throw new BranchServiceException("Error fetching all appointments. Check the input data and try again", ex) {};
        }
    }

    public List<BankerDTO> getBankerById(Integer bankerId) {
        logger.info("Fetching bankerId with ID: {}", bankerId);
        return Collections.singletonList(
                bankerRepository.findByBankerId(bankerId)
                        .map(bankerMapper::bankerToBankerDTO)
                        .orElseThrow(() -> new EntityNotFoundException("Banker not found with id: " + bankerId))  // standardize exception handling
        );
    }

    /**
     * Retrieves a list of BankerDTO objects associated with the specified branch ID.
     *
     * <p>This method queries the repository for bankers belonging to the branch with the given ID,
     * maps the resulting entities to BankerDTO objects, and returns a list of BankerDTOs.
     *
     * @param branchId The ID of the branch for which to retrieve bankers.
     * @return A list of BankerDTO objects representing bankers associated with the specified branch ID.
     *
     * @throws BankerServiceException If an error occurs during the banker retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed banker retrieval attempt is logged.
     */
    public List<BankerDTO> getBankersByBranchId(Integer branchId) {
        try {
            logger.info("Fetching bankers for branch with ID: {}", branchId);
            List<Banker> bankers = bankerRepository.findBankersByBranch_BranchId(branchId);
            return bankerMapper.bankerListToBankerDTOList(bankers);
        } catch (Exception ex) {
            logger.error("Error fetching bankers of branch with ID {}: {}", branchId, ex.getMessage(), ex);
            throw new BankerServiceException("Failed to fetch bankers.  Check the input data and try again.", ex);
        }
    }

    public List<BankerDTO> getAllBankersSorted(String sortBy, String sortOrder) {
        try {
            logger.info("Sorting Bankers by: {}, in order: {}. Timestamp: {}", sortBy, sortOrder, LocalDateTime.now());
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

            if ("branchId".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "branchId"));
            } else if ("firstName".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "firstName"));
            } else if ("lastName".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "lastName"));
            } else if ("jobTitle".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "jobTitle"));
            }

            return bankerRepository.findAll(sort)
                    .stream()
                    .map(bankerMapper::bankerToBankerDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error fetching all sorted bankers: {}", ex.getMessage(), ex);
            throw new BankerServiceException("Error fetching all sorted bankers. Check the input data and try again", ex);
        }
    }

    public Page<BankerDTO> getFilteredBankers(
            Integer branchId, String firstName, String lastName, String jobTitle, Pageable pageable) {

        return bankerRepository.findBankersByBranchIdAndFirstNameAndLastName(
                        branchId, firstName, lastName, jobTitle, pageable)
                .map(bankerMapper::bankerToBankerDTO);
    }

    /**
     * Creates a new banker based on the provided BankerDTO.
     *
     * <p>This method creates a new banker by converting the BankerDTO to a Banker entity,
     * associating it with the specified branch if the branchId is provided, and then saving it to the repository.
     * The saved banker is then converted back to a BankerDTO for return.
     *
     * @param bankerDTO The BankerDTO containing information about the banker to be created.
     *                 It is recommended to include at least the necessary parameters like name and contact information.
     *                 If branchId is provided, the banker will be associated with the corresponding branch.
     * @return The created BankerDTO representing the newly created banker.
     *
     * @throws BankerServiceException If an error occurs during the banker creation process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed banker creation attempt is logged.
     */
    @Transactional
    public BankerDTO createBanker(BankerDTO bankerDTO) {  // do I need to pass the entire DTO?  Or just the necessary parameters?
        try {
            logger.info("Creating new banker.");
            Banker banker = bankerMapper.bankerDTOToBanker(bankerDTO);

            if (bankerDTO.getBranchId() != null) {
                Branch existingBranch = branchRepository.findById(bankerDTO.getBranchId())
                        .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + bankerDTO.getBranchId()));
                banker.setBranch(existingBranch);
            }
            Banker savedBanker = bankerRepository.save(banker);
            BankerDTO savedBankerDTO =  bankerMapper.bankerToBankerDTO(savedBanker);

            logger.info("Banker created successfully: {}, Timestamp: {}", savedBankerDTO, LocalDateTime.now());
            return savedBankerDTO;
        } catch (DataAccessException ex) {
            logger.error("Data access issue while creating banker: {}", ex.getMessage(), ex);
            throw new BankerServiceException("Failed to create banker due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error creating banker: {}", ex.getMessage(), ex);
            logger.error("Failed to create banker with data: {}", bankerDTO);
            throw new BankerServiceException("Failed to create banker. Check the input data and try again.", ex);
        }
    }

    @Transactional
    public BankerDTO updateBanker(Integer bankerId, BankerDTO updatedBankerDTO) {
        Banker existingBanker = bankerRepository.findByBankerId(bankerId)
                .orElseThrow(() -> new EntityNotFoundException("Banker not found with ID: " + bankerId));

        Banker updatedBanker = bankerMapper.bankerDTOToBanker(updatedBankerDTO);
        updateExistingBanker(existingBanker, updatedBanker);

        Banker savedBanker = bankerRepository.save(existingBanker);
        return bankerMapper.bankerToBankerDTO(savedBanker);
    }

    private void updateExistingBanker(Banker existingBanker, Banker updatedBanker) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(updatedBanker, existingBanker);
    }

    /**
     * Deletes a banker based on the specified banker ID.
     *
     * <p>This method deletes the banker with the given ID from the repository. If the banker is not found,
     * a BankerNotFoundException is thrown. The method operates within a transactional context.
     *
     * @param bankerId The ID of the banker to be deleted.
     *
     * @throws BankerNotFoundException If the banker with the specified ID is not found in the repository.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed banker deletion attempt is logged.
     */
    @Transactional  // when deleting a banker, it needs to cascade to appointments...to new banker?  notify user?
    public void deleteBankerById(Integer bankerId) {
        try {
            logger.info("Deleting banker with ID: {}", bankerId);
            bankerRepository.deleteById(bankerId);
            logger.info("Banker with ID {} deleted successfully", bankerId);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Error deleting banker with ID {}: Banker not found", bankerId);
            throw new BankerNotFoundException("Banker not found with ID: " + bankerId, ex);
        } catch (Exception ex) {
            logger.error("Error deleting banker with ID {}: {}", bankerId, ex.getMessage(), ex);
            throw new BankerNotFoundException("Failed to delete banker. Check the input data and try again.", ex);
        }
    }
}
