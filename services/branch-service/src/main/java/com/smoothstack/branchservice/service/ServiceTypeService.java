package com.smoothstack.branchservice.service;

import com.smoothstack.branchservice.dto.ServiceTypeDTO;
import com.smoothstack.branchservice.exception.custom.ServiceTypeNotFoundException;
import com.smoothstack.branchservice.exception.custom.ServiceTypeServiceException;
import com.smoothstack.branchservice.mapper.ServiceTypeMapper;
import com.smoothstack.branchservice.model.ServiceType;
import com.smoothstack.branchservice.dao.ServiceTypeRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    /**
     * Retrieves a list of ServiceTypeDTO objects based on the specified parameters.
     *
     * <p>This method allows fetching service types based on various criteria such as serviceTypeId or branchId.
     * If both parameters are null, it returns a list of all service types.
     * If serviceTypeId is provided, it retrieves the specific service type with that ID.
     * If branchId is provided, it retrieves all service types associated with the specified branch.
     *
     * @param serviceTypeId The ID of the specific service type to retrieve. If provided, branchId is ignored.
     * @param serviceTypeName The ID of the branch for which to retrieve service types. Ignored if serviceTypeId is specified.
     * @return A list of ServiceTypeDTO objects representing service types based on the provided parameters.
     */
    public List<ServiceTypeDTO> getServiceTypesByParams(Integer serviceTypeId, String serviceTypeName) {
        if (serviceTypeId != null) {
            return getServiceTypeById(serviceTypeId);
        }
        else if (serviceTypeName != null) {
            return getServiceTypesByServiceTypeName(serviceTypeName);
        }
        else {
            return getAllServiceTypes();
        }
    }

    /**
     * Retrieves a list of all ServiceTypeDTO objects representing all service types.
     *
     * <p>This method fetches all service types from the repository, maps the resulting entities to ServiceTypeDTO objects,
     * and returns a list of ServiceTypeDTOs.
     *
     * @return A list of ServiceTypeDTO objects representing all service types.
     *
     * @throws ServiceTypeServiceException If an error occurs during the service type retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed service type retrieval attempt is logged.
     */
    public List<ServiceTypeDTO> getAllServiceTypes() {
        try {
            logger.info("Fetching all service types.");
            return serviceTypeRepository.findAll()
                    .stream()
                    .map(serviceTypeMapper::serviceTypeToServiceTypeDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Error fetching all service types: {}", ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Error fetching all service types. Check the input data and try again", ex) {};
        }
    }

    /**
     * Retrieves a list of ServiceTypeDTO objects associated with the specified service type ID.
     *
     * <p>This method queries the repository for service types with the given ID, maps the resulting entities to ServiceTypeDTO objects,
     * and returns a list of ServiceTypeDTOs.
     *
     * @param serviceId The ID of the service type for which to retrieve information.
     * @return A list of ServiceTypeDTO objects representing service types with the specified ID.
     *
     * @throws ServiceTypeServiceException If an error occurs during the service type retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed service type retrieval attempt is logged.
     */
    public List<ServiceTypeDTO> getServiceTypeById(Integer serviceId) {
        try {
            logger.info("Fetching service type with ID: {}", serviceId);
            return serviceTypeRepository.findByServiceId(serviceId)
                    .stream()
                    .map(serviceTypeMapper::serviceTypeToServiceTypeDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Data access issue while fetching service type: {}", ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Failed to fetch service type due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error fetching service type with ID {}: {}", serviceId, ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Failed to fetch service type. Check the input data and try again.", ex);
        }
    }

    /**
     * Retrieves a list of ServiceTypeDTO objects associated with the specified branch ID.
     *
     * <p>This method queries the repository for service types belonging to the branch with the given ID,
     * maps the resulting entities to ServiceTypeDTO objects, and returns a list of ServiceTypeDTOs.
     *
     * @param serviceTypeName The ID of the branch for which to retrieve service types.
     * @return A list of ServiceTypeDTO objects representing service types associated with the specified branch ID.
     *
     * @throws ServiceTypeServiceException If an error occurs during the service type retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed service type retrieval attempt is logged.
     */
    public List<ServiceTypeDTO> getServiceTypesByServiceTypeName(String serviceTypeName) {
        try {
            logger.info("Fetching service types with service type name: {}", serviceTypeName);
            List<ServiceType> serviceTypes = serviceTypeRepository.findServiceTypesByServiceTypeName(serviceTypeName);
            return serviceTypeMapper.serviceTypeListToServiceTypeDTOList(serviceTypes);
        } catch (Exception ex) {
            logger.error("Error fetching service types of branch with ID {}: {}", serviceTypeName, ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Failed to fetch service types.  Check the input data and try again.", ex);
        }
    }

    public List<ServiceTypeDTO> getAllServiceTypesSorted(String sortBy, String sortOrder) {
        try {
            logger.info("Sorting Service Types by: {}, in order: {}. Timestamp: {}", sortBy, sortOrder, LocalDateTime.now());
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

            if ("branchId".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "branchId"));
            } else if ("serviceTypeName".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "serviceTypeName"));
            }

            return serviceTypeRepository.findAll(sort)
                    .stream()
                    .map(serviceTypeMapper::serviceTypeToServiceTypeDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error fetching all sorted service types: {}", ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Error fetching all sorted appointments. Check the input data and try again", ex);
        }
    }

    public Page<ServiceTypeDTO> getFilteredServiceTypes(
            Integer branchId, String serviceTypeName, Pageable pageable) {

        return serviceTypeRepository.findByBranches_BranchIdAndServiceTypeName(
                        branchId, serviceTypeName, pageable)
                .map(serviceTypeMapper::serviceTypeToServiceTypeDTO);
    }

    /**
     * Creates a new service type based on the provided ServiceTypeDTO.
     *
     * <p>This method converts the ServiceTypeDTO to a ServiceType entity, saves it to the repository, and then
     * converts the saved entity back to a ServiceTypeDTO for return. The method operates within a transactional context.
     *
     * @param serviceTypeDTO The ServiceTypeDTO containing information about the service type to be created.
     *                       It is recommended to include at least the necessary parameters like name and description.
     * @return The created ServiceTypeDTO representing the newly created service type.
     *
     * @throws ServiceTypeServiceException If an error occurs during the service type creation process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed service type creation attempt is logged.
     */
    @Transactional
    public ServiceTypeDTO createServiceType(ServiceTypeDTO serviceTypeDTO) {
        try {
            logger.info("Creating new service type.");
            ServiceType serviceType = serviceTypeMapper.serviceTypeDTOToServiceType(serviceTypeDTO);
            ServiceType savedServiceType = serviceTypeRepository.save(serviceType);
            ServiceTypeDTO savedServiceTypeDTO = serviceTypeMapper.serviceTypeToServiceTypeDTO(savedServiceType);
            logger.info("Service Type created successfully: {}", savedServiceTypeDTO);
            return savedServiceTypeDTO;
        } catch (DataAccessException ex) {
            logger.error("Data access issue while creating service type: {}", ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Failed to create service type due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error creating service type: {}", ex.getMessage(), ex);
            logger.error("Failed to create service type with data: {}", serviceTypeDTO);
            throw new ServiceTypeServiceException("Failed to create service type. Check the input data and try again.", ex);
        }
    }

    @Transactional
    public ServiceTypeDTO updateServiceType(Integer serviceId, ServiceTypeDTO updatedServiceTypeDTO) {
        ServiceType existingServiceType = serviceTypeRepository.findByServiceId(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service Type not found with ID: " + serviceId));

        ServiceType updatedServiceType = serviceTypeMapper.serviceTypeDTOToServiceType(updatedServiceTypeDTO);
        updateExistingServiceType(existingServiceType, updatedServiceType);

        ServiceType savedServiceType = serviceTypeRepository.save(existingServiceType);
        return serviceTypeMapper.serviceTypeToServiceTypeDTO(savedServiceType);
    }

    private void updateExistingServiceType(ServiceType existingServiceType, ServiceType updatedServiceType) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(updatedServiceType, existingServiceType);
    }

    /**
     * Deletes a service type based on the specified service type ID.
     *
     * <p>This method deletes the service type with the given ID from the repository. It operates within a transactional context.
     *
     * @param serviceId The ID of the service type to be deleted.
     *
     * @throws ServiceTypeNotFoundException If the service type with the specified ID is not found in the repository.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed service type deletion attempt is logged.
     *
     * @throws ServiceTypeServiceException If an error occurs during the service type deletion process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed service type deletion attempt is logged.
     */
    @Transactional
    public void deleteServiceTypeById(Integer serviceId) {
        try {
            logger.info("Deleting service type with ID: {}", serviceId);
            serviceTypeRepository.deleteById(serviceId);
            logger.info("Service Type with ID {} deleted successfully", serviceId);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Error deleting service type with ID {}: Service Type not found", serviceId);
            throw new ServiceTypeNotFoundException("Service Type not found with ID: " + serviceId, ex);
        } catch (Exception ex) {
            logger.error("Error deleting service type with ID {}: {}", serviceId, ex.getMessage(), ex);
            throw new ServiceTypeServiceException("Failed to delete service type. Check the input data and try again.", ex);
        }
    }
}
