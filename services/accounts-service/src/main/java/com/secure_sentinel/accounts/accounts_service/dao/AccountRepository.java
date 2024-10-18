package com.secure_sentinel.accounts.accounts_service.dao;

import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.model.enums.AccountType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    List<Account> findByUserId(Integer userId);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUserIdAndIsActive(Integer userId, Boolean isActive);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findByIdWithPessimisticLock(Integer accountId);

    default Page<Account> findAllWithFilters(String accountType, Double minBalance, Double maxBalance,
                                             Boolean isActive, LocalDateTime startDate, LocalDateTime endDate,
                                             Pageable pageable) {

        return findAll((Specification<Account>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(accountType)) {
                predicates.add(cb.equal(root.get("accountType"), AccountType.valueOf(accountType)));
            }
            if (minBalance != null) {
                predicates.add(cb.ge(root.get("balance"), minBalance));
            }
            if (maxBalance != null) {
                predicates.add(cb.le(root.get("balance"), maxBalance));
            }
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }
            // convert list of predicates to an array and combine with AND operator
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
}