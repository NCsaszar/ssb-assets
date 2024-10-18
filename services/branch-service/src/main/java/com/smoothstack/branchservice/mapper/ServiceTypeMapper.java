package com.smoothstack.branchservice.mapper;

import com.smoothstack.branchservice.dto.ServiceTypeDTO;
import com.smoothstack.branchservice.model.ServiceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {

    @Mapping(target = "serviceId", ignore = true)
    ServiceType serviceTypeDTOToServiceType(ServiceTypeDTO serviceTypeDTO);

    @Mapping(source = "serviceId", target = "serviceId")
    ServiceTypeDTO serviceTypeToServiceTypeDTO(ServiceType serviceType);

    List<ServiceTypeDTO> serviceTypeListToServiceTypeDTOList(List<ServiceType> serviceType);

}
