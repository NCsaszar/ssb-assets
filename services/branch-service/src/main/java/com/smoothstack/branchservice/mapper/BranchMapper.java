package com.smoothstack.branchservice.mapper;

import com.smoothstack.branchservice.dto.BranchDTO;
import com.smoothstack.branchservice.model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "branchId", ignore = true)
    Branch branchDTOToBranch(BranchDTO branchDTO);

    @Mapping(target = "branchId", source = "branchId")
    BranchDTO branchToBranchDTO(Branch branch);
}