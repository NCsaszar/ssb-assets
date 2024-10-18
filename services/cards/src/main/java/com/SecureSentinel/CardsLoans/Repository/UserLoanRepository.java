package com.SecureSentinel.CardsLoans.Repository;

import com.SecureSentinel.CardsLoans.Model.UserLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLoanRepository extends JpaRepository<UserLoan,Integer> {
    List<UserLoan> findActiveUserLoansByAccountID(Integer accountID);
    List<UserLoan> findUserLoansByUserID(Integer accountID);

    @Query("SELECT ul FROM UserLoan ul " +
            "WHERE (:userLoanID IS NULL OR ul.userLoanID = :userLoanID) " +
            "AND (:userID IS NULL OR ul.userID = :userID) " +
            "AND (:accountID IS NULL OR ul.accountID = :accountID)")
    List<UserLoan> findUserLoansByUserLoanIDAndUserIDAndAccountID(@Param("userLoanID") Integer userLoanID,@Param("userID") Integer userID,@Param("accountID") Integer accountID);
}
