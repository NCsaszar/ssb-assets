package com.smoothstack.branchservice.dao;

import com.smoothstack.branchservice.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Integer> {

    Optional<Queue> findByQueueId(Integer queueId);

    List<Queue> findQueuesByBranch_BranchId(Integer branchId);

    List<Queue> findQueuesByUserId(Integer userId);

}