package com.SecureSentinel.CardsLoans.Repository;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Integer> {
    @Query("SELECT l FROM Loan l INNER JOIN l.loanType lt" +
            " WHERE (:loanTypeName is NULL OR l.loanType.loanTypeName = :loanTypeName)" +
            " AND (:loanID is NULL OR l.loanID = :loanID)")
    List<Loan> findLoanByLoanTypeNameOOrLoanID(@Param("loanTypeName") String loanTypeName,@Param("loanID") Integer loanID);

}
