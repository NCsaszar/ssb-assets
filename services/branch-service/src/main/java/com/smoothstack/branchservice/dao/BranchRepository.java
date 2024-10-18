package com.smoothstack.branchservice.dao;

import com.smoothstack.branchservice.model.Branch;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Optional<Branch> findByBranchId(Integer branchId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Branch> findAndLockByBranchId(Integer branchId); // use this instead of manual locking

    List<Branch> findAll(Sort sort);

    Page<Branch> findByBranchNameAndBranchCode(
            String branchName,
            String branchCode,
            String address1,
            String city,
            String postalCode,
            Pageable pageable
    );

}