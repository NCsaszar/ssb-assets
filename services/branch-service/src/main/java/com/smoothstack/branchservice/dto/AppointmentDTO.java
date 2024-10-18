package com.smoothstack.branchservice.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {

    private Integer appointmentId;

    @NotNull
    private Integer branchId;

    @NotNull
    private Integer bankerId;

    @NotNull
    private Integer userId;

    private Integer serviceId;

    @NotBlank
    private LocalDateTime timeslot;

    private String description;

}