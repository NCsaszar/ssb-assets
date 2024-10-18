package com.smoothstack.branchservice.service;

import com.smoothstack.branchservice.dto.AppointmentDTO;
import com.smoothstack.branchservice.exception.custom.AppointmentNotFoundException;
import com.smoothstack.branchservice.exception.custom.AppointmentServiceException;
import com.smoothstack.branchservice.mapper.AppointmentMapper;
import com.smoothstack.branchservice.model.Appointment;
import com.smoothstack.branchservice.model.Banker;
import com.smoothstack.branchservice.model.Branch;
import com.smoothstack.branchservice.model.ServiceType;
import com.smoothstack.branchservice.dao.AppointmentRepository;
import com.smoothstack.branchservice.dao.BankerRepository;
import com.smoothstack.branchservice.dao.BranchRepository;
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
import java.util.*;

@AllArgsConstructor
@Service
public class AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final BranchRepository branchRepository;
    private final BankerRepository bankerRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    /**
     * Retrieves a list of AppointmentDTO objects based on the specified parameters.
     *
     * <p>This method allows fetching appointments based on various criteria such as appointmentId, branchId, userId, or bankerId.
     * If multiple parameters are provided, the method prioritizes them in the order: appointmentId, branchId, userId, and bankerId.
     * If none of the parameters are specified, it returns all appointments.
     *
     * @param appointmentId The ID of the specific appointment to retrieve. If provided, other parameters are ignored.
     * @param branchId      The ID of the branch for which to retrieve appointments. Ignored if appointmentId is specified.
     * @param userId        The ID of the user for whom to retrieve appointments. Ignored if appointmentId is specified.
     * @param bankerId      The ID of the banker for whom to retrieve appointments. Ignored if appointmentId is specified.
     * @return A list of AppointmentDTO objects representing appointments based on the provided parameters.
     */
    public List<AppointmentDTO> getAppointmentsByParams(Integer appointmentId, Integer branchId, Integer userId, Integer bankerId) {
        Set<AppointmentDTO> uniqueAppointments = new HashSet<>();

        if (appointmentId != null) {
            uniqueAppointments.addAll(getAppointmentById(appointmentId));
        }
        else if (branchId != null) {
            uniqueAppointments.addAll(getAppointmentsByBranchId(branchId));
        }
        else if (userId != null) {
            uniqueAppointments.addAll(getAppointmentsByUserId(userId));
        }
        else if (bankerId != null) {
            uniqueAppointments.addAll(getAppointmentsByBankerId(bankerId));
        }
        else {
            uniqueAppointments.addAll(getAllAppointments());
        }
        return new ArrayList<>(uniqueAppointments);
    }

    /**
     * Retrieves a list of all AppointmentDTO objects representing all appointments.
     *
     * <p>This method fetches all appointments from the repository, maps the resulting entities to AppointmentDTO objects,
     * and returns a list of AppointmentDTOs.
     *
     * @return A list of AppointmentDTO objects representing all appointments.
     *
     * @throws AppointmentServiceException If an error occurs during the appointment retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment retrieval attempt is logged.
     */
    public List<AppointmentDTO> getAllAppointments() {
        try {
            logger.info("Fetching all appointments.");
            return appointmentRepository.findAll()
                    .stream()
                    .map(appointmentMapper::appointmentToAppointmentDTO)
                    .toList();
        } catch (DataAccessException ex) {
            logger.error("Error fetching all appointments: {}", ex.getMessage(), ex);
            throw new AppointmentServiceException("Error fetching all appointments. Check the input data and try again", ex) {};
        }
    }

    /**
     * Retrieves a list of AppointmentDTO objects associated with the specified appointment ID.
     *
     * <p>This method queries the repository for appointments with the given ID, maps the resulting entities to
     * AppointmentDTO objects, and returns a list of AppointmentDTOs. The appointment ID is expected to be unique.
     *
     * @param appointmentId The ID of the appointment for which to retrieve information.
     * @return A list of AppointmentDTO objects representing appointments with the specified ID.
     *
     * @throws AppointmentServiceException If an error occurs during the appointment retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment retrieval attempt is logged.
     */
    public List<AppointmentDTO> getAppointmentById(Integer appointmentId) {
        try {
            logger.info("Fetching appointment with ID: {}", appointmentId);
            return appointmentRepository.findByAppointmentId(appointmentId)
                    .stream()
                    .map(appointmentMapper::appointmentToAppointmentDTO)
                    .toList();
        } catch (DataAccessException ex) {
            logger.error("Data access issue while fetching appointment: {}", ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to fetch appointment due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error fetching appointment with ID {}: {}", appointmentId, ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to fetch appointment. Check the input data and try again.", ex);
        }
    }

    /**
     * Retrieves a list of AppointmentDTO objects associated with the specified branch ID.
     *
     * <p>This method queries the repository for appointments belonging to the branch with the given ID,
     * maps the resulting entities to AppointmentDTO objects, and returns a list of AppointmentDTOs.
     *
     * @param branchId The ID of the branch for which to retrieve appointments.
     * @return A list of AppointmentDTO objects representing appointments associated with the specified branch ID.
     *
     * @throws AppointmentServiceException If an error occurs during the appointment retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment retrieval attempt is logged.
     */
    public List<AppointmentDTO> getAppointmentsByBranchId(Integer branchId) {
        try {
            logger.info("Fetching appointments for branch with ID: {}", branchId);
            List<Appointment> appointments = appointmentRepository.findAppointmentsByBranch_BranchId(branchId);
            return appointmentMapper.appointmentListToAppointmentDTOList(appointments);
        } catch (Exception ex) {
            logger.error("Error fetching appointments of branch with ID {}: {}", branchId, ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to fetch appointments.  Check the input data and try again.", ex);
        }
    }

    /**
     * Retrieves a list of AppointmentDTO objects associated with the specified user ID.
     *
     * <p>This method queries the repository for appointments belonging to the user with the given ID,
     * maps the resulting entities to AppointmentDTO objects, and returns a list of AppointmentDTOs.
     *
     * @param userId The ID of the user for whom to retrieve appointments.
     * @return A list of AppointmentDTO objects representing appointments associated with the specified user ID.
     *
     * @throws AppointmentServiceException If an error occurs during the appointment retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment retrieval attempt is logged.
     */
    public List<AppointmentDTO> getAppointmentsByUserId(Integer userId) {
        try {
            logger.info("Fetching appointments for user with ID: {}", userId);
            List<Appointment> appointments = appointmentRepository.findAppointmentsByUserId(userId);
            return appointmentMapper.appointmentListToAppointmentDTOList(appointments);
        } catch (Exception ex) {
            logger.error("Error fetching appointments of user with ID {}: {}", userId, ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to fetch appointments.  Check the input data and try again.", ex);
        }
    }

    /**
     * Retrieves a list of AppointmentDTO objects associated with the specified banker ID.
     *
     * <p>This method queries the repository for appointments belonging to the banker with the given ID,
     * maps the resulting entities to AppointmentDTO objects, and returns a list of AppointmentDTOs.
     *
     * @param bankerId The ID of the banker for whom to retrieve appointments.
     * @return A list of AppointmentDTO objects representing appointments associated with the specified banker ID.
     *
     * @throws AppointmentServiceException If an error occurs during the appointment retrieval process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment retrieval attempt is logged.
     */
    public List<AppointmentDTO> getAppointmentsByBankerId(Integer bankerId) {
        try {
            logger.info("Fetching appointments for banker with ID: {}", bankerId);
            List<Appointment> appointments = appointmentRepository.findAppointmentsByBanker_BankerId(bankerId);
            return appointmentMapper.appointmentListToAppointmentDTOList(appointments);
        } catch (Exception ex) {
            logger.error("Error fetching appointments of banker with ID {}: {}", bankerId, ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to fetch appointments.  Check the input data and try again.", ex);
        }
    }

    public List<AppointmentDTO> getAllAppointmentsSorted(String sortBy, String sortOrder) {
        try {
            logger.info("Sorting Appointments by: {}, in order: {}. Timestamp: {}", sortBy, sortOrder, LocalDateTime.now());
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

            if ("branchId".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "branchId"));
            } else if ("bankerId".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "bankerId"));
            } else if ("userId".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "userId"));
            } else if ("startTime".equals(sortBy)) {
                sort = sort.and(Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "startTime"));
            }

            return appointmentRepository.findAll(sort)
                    .stream()
                    .map(appointmentMapper::appointmentToAppointmentDTO)
                    .toList();
        } catch (Exception ex) {
            logger.error("Error fetching all sorted appointments: {}", ex.getMessage(), ex);
            throw new AppointmentServiceException("Error fetching all sorted appointments. Check the input data and try again", ex);
        }
    }

    public Page<AppointmentDTO> getFilteredAppointments(
    Integer branchId, Integer bankerId, Integer userId, LocalDateTime timeslot,
            Pageable pageable
    ) {

        logger.info("Fetching filtered appointments.");
        return appointmentRepository.findByBranchIdAndBankerIdAndUserIdAndTimeslot(
                        branchId, bankerId, userId, timeslot,
                        pageable
                )
                .map(appointmentMapper::appointmentToAppointmentDTO);
    }

    public List<AppointmentDTO> getAppointmentsFilteredByBankerIdAndTimeslot(
            Integer bankerId,
            LocalDateTime timeslot
    ) {
        logger.info("Fetching filtered appointments.");
        return appointmentRepository.findByBankerIdAndTimeslot(bankerId, timeslot)
                .stream()
                .map(appointmentMapper::appointmentToAppointmentDTO)
                .toList();
    }

    public List<AppointmentDTO> getAppointmentsFilteredByBranchIdAndTimeslot(
            Integer branchId,
            LocalDateTime timeslot
    ) {
        logger.info("Fetching filtered appointments.");
        return appointmentRepository.findByBranchIdAndTimeslot(branchId, timeslot)
                .stream()
                .map(appointmentMapper::appointmentToAppointmentDTO)
                .toList();
    }

    /**
     * Creates a new appointment based on the provided AppointmentDTO.
     *
     * <p>This method creates a new appointment by converting the AppointmentDTO to an Appointment entity,
     * associating it with the specified branch and banker if their IDs are provided, and then saving it to the repository.
     * The saved appointment is then converted back to an AppointmentDTO for return.
     *
     * @param appointmentDTO The AppointmentDTO containing information about the appointment to be created.
     *                      It is recommended to include at least the necessary parameters like date, time, and customer information.
     *                      If branchId and bankerId are provided, the appointment will be associated with the corresponding branch and banker.
     * @return The created AppointmentDTO representing the newly created appointment.
     *
     * @throws AppointmentServiceException If an error occurs during the appointment creation process.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment creation attempt is logged.
     */
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {  // don't pass entire DTO, only necessary params
        try {
            logger.info("Creating new appointment.");
            Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);

            Optional<Appointment> lockedAppointment = appointmentRepository.findByTimeslot(appointment.getTimeslot());
            if (lockedAppointment.isPresent()) {
                throw new AppointmentServiceException("The selected timeslot is already locked. Please choose a different timeslot.");
            }

            if (appointmentDTO.getBranchId() != null) {
                Branch existingBranch = branchRepository.findById(appointmentDTO.getBranchId())
                        .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + appointmentDTO.getBranchId()));
                appointment.setBranch(existingBranch);
            }
;
            if (appointmentDTO.getBankerId() != null) {
                Banker existingBanker = bankerRepository.findById(appointmentDTO.getBankerId())
                        .orElseThrow(() -> new EntityNotFoundException("Banker not found with id: " + appointmentDTO.getBankerId()));
                appointment.setBanker(existingBanker);
            }
            if (appointmentDTO.getServiceId() != null) {  // no longer demanding ServiceType, but need timeslot
                ServiceType existingServiceType = serviceTypeRepository.findById(appointmentDTO.getServiceId())
                        .orElseThrow(() -> new EntityNotFoundException("Service Type not found with id: " + appointmentDTO.getServiceId()));
                appointment.setServiceType(existingServiceType);
            }

            appointment.setUserId(appointmentDTO.getUserId());
            appointment.setTimeslot(appointmentDTO.getTimeslot());
            appointment.setDescription(appointmentDTO.getDescription());

            Appointment savedAppointment = appointmentRepository.save(appointment);
            AppointmentDTO savedAppointmentDTO = appointmentMapper.appointmentToAppointmentDTO(savedAppointment);

            logger.info("Appointment created successfully: {}", savedAppointmentDTO);
            return savedAppointmentDTO;
        } catch (DataAccessException ex) {
            logger.error("Data access issue while creating appointment: {}", ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to create appointment due to a data access issue. Please try again later.", ex);
        } catch (Exception ex) {
            logger.error("Error creating appointment: {}", ex.getMessage(), ex);
            logger.error("Failed to create appointment with data: {}", appointmentDTO);
            throw new AppointmentServiceException("Failed to create appointment. Check the input data and try again.", ex);
        }
    }

    @Transactional
    public AppointmentDTO updateAppointment(Integer appointmentId, AppointmentDTO updatedAppointmentDTO) {
        try {
            logger.info("Updating appointment with ID: {}", appointmentId);
            Appointment existingAppointment = appointmentRepository.findByAppointmentId(appointmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));
            Appointment updatedAppointment = appointmentMapper.appointmentDTOToAppointment(updatedAppointmentDTO);
            updateExistingAppointment(existingAppointment, updatedAppointment);
            Appointment savedAppointment = appointmentRepository.save(existingAppointment);
            AppointmentDTO savedAppointmentDTO = appointmentMapper.appointmentToAppointmentDTO(savedAppointment);
            logger.info("Appointment updated successfully: {}", savedAppointmentDTO);
            return savedAppointmentDTO;
        } catch (EntityNotFoundException ex) {
            logger.error("Error updating appointment with ID {}: Appointment not found", appointmentId);
            throw new AppointmentServiceException("Failed to update Appointment due to an entity not found issue. Check the input data and try again.", ex);
        } catch (Exception ex) {
            logger.error("Error updating appointment with ID {}: {}", appointmentId, ex.getMessage(), ex);
            throw new AppointmentServiceException("Failed to update appointment. Check the input data and try again.", ex);
        }
    }

    private void updateExistingAppointment(Appointment existingAppointment, Appointment updatedAppointment) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(updatedAppointment, existingAppointment);
    }

    /**
     * Deletes an appointment based on the specified appointment ID.
     *
     * <p>This method deletes the appointment with the given ID from the repository. If the appointment is not found,
     * an AppointmentNotFoundException is thrown. The method operates within a transactional context.
     *
     * @param appointmentId The ID of the appointment to be deleted.
     *
     * @throws AppointmentNotFoundException If the appointment with the specified ID is not found in the repository.
     *   Check the exception message for details on the cause of the error.
     *   Additionally, relevant information about the failed appointment deletion attempt is logged.
     */
    @Transactional
    public void deleteAppointmentById(Integer appointmentId) {
        try {
            logger.info("Deleting appointment with ID: {}", appointmentId);
            appointmentRepository.deleteById(appointmentId);
            logger.info("Appointment with ID {} deleted successfully", appointmentId);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Error deleting appointment with ID {}: Appointment not found", appointmentId);
            throw new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId, ex);
        } catch (Exception ex) {
            logger.error("Error deleting appointment with ID {}: {}", appointmentId, ex.getMessage(), ex);
            throw new AppointmentNotFoundException("Failed to delete appointment. Check the input data and try again.", ex);
        }
    }
}