package com.smoothstack.branchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class QueueDTO {

    private Integer queueId;

    @NotNull
    private Integer branchId;

    @NotNull
    private Integer userId;

    @NotBlank
    private Timestamp checkinTime;
}
