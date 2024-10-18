package com.secure_sentinel.accounts.accounts_service.dao;

import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId " +
            "AND (COALESCE(:startDate, t.dateTime) <= t.dateTime) " +
            "AND (COALESCE(:endDate, t.dateTime) >= t.dateTime) " +
            "AND (COALESCE(:minAmount, t.amount) <= t.amount) " +
            "AND (COALESCE(:maxAmount, t.amount) >= t.amount) " +
            "AND (COALESCE(:transactionType, t.transactionType) = t.transactionType) ")
    Page<Transaction> findAllWithFilters(@Param("accountId") Integer accountId,
                                         @Param("startDate") Timestamp startDate,
                                         @Param("endDate") Timestamp endDate,
                                         @Param("minAmount") Double minAmount,
                                         @Param("maxAmount") Double maxAmount,
                                         @Param("transactionType") TransactionType transactionType,
                                         Pageable pageable);

    @Query("SELECT t FROM Transaction t " +
            "WHERE (COALESCE(:startDate, t.dateTime) <= t.dateTime) " +
            "AND (COALESCE(:endDate, t.dateTime) >= t.dateTime) " +
            "AND (COALESCE(:minAmount, t.amount) <= t.amount) " +
            "AND (COALESCE(:maxAmount, t.amount) >= t.amount) " +
            "AND (COALESCE(:transactionType, t.transactionType) = t.transactionType)" +
            "AND (:lastFour IS NULL OR t.account.accountNumber LIKE %:lastFour)")
    Page<Transaction> findAllWithFiltersAdmin(@Param("startDate") Timestamp startDate,
                                              @Param("endDate") Timestamp endDate,
                                              @Param("minAmount") Double minAmount,
                                              @Param("maxAmount") Double maxAmount,
                                              @Param("transactionType") TransactionType transactionType,
                                              @Param("lastFour") String lastFour,
                                              Pageable pageable);


    List<Transaction> findAllByAccountAccountIdOrderByDateTimeAsc(Integer accountId);

    List<Transaction> findTop10ByAccountAccountIdOrderByDateTimeDesc(Integer accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId " +
            "AND t.dateTime >= :startDate AND t.dateTime <= :endDate ORDER BY t.dateTime ASC")
    List<Transaction> findAllByAccountAndDateRange(@Param("accountId") Integer accountId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}