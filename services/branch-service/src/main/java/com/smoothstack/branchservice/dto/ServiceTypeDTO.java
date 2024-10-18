package com.smoothstack.branchservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceTypeDTO {

    private Integer serviceId;

    @NotBlank
    private String serviceTypeName;

    @NotBlank
    private String description;
}
