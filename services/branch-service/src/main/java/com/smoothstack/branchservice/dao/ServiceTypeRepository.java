package com.smoothstack.branchservice.dao;

import com.smoothstack.branchservice.model.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {

    Optional<ServiceType> findByServiceId(Integer serviceId);

    List<ServiceType> findServiceTypesByServiceTypeName(String serviceTypeName);

    Page<ServiceType> findByBranches_BranchIdAndServiceTypeName(
            Integer branchId,
            String serviceTypeName,
            Pageable pageable
    );
}