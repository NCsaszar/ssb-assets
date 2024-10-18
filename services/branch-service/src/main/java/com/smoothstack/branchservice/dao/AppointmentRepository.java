package com.smoothstack.branchservice.dao;

import com.smoothstack.branchservice.model.Appointment;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Optional<Appointment> findByAppointmentId(Integer appointmentId);

    List<Appointment> findAppointmentsByBranch_BranchId(Integer branchId);

    List<Appointment> findAppointmentsByUserId(Integer userId);

    List<Appointment> findAppointmentsByBanker_BankerId(Integer bankerId);

    Page<Appointment> findByBranchIdAndBankerIdAndUserIdAndTimeslot(

        Integer branchId,
        Integer bankerId,
        Integer userId,
        LocalDateTime timeslot,
        Pageable pageable
    );

    List<Appointment> findByBankerIdAndTimeslot(

            Integer bankerId,
            LocalDateTime timeslot
    );

    List<Appointment> findByBranchIdAndTimeslot(

            Integer branchId,
            LocalDateTime timeslot
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Appointment> findByTimeslot(LocalDateTime timeslot);

}