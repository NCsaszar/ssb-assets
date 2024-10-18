package com.secure_sentinel.accounts.accounts_service.dao;

import com.secure_sentinel.accounts.accounts_service.model.AccountProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountProgramRepository extends JpaRepository<AccountProgram, Integer> {
    Optional<AccountProgram> findByProgramNameAndAccountType(String programName, String accountType);
}