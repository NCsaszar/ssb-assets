package com.SecureSentinel.CardsLoans.Mapper;


import com.SecureSentinel.CardsLoans.DTO.*;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.LoanType;
import com.SecureSentinel.CardsLoans.Model.UserLoan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {


    @Mapping(target = "loanType",source = "loanType.loanTypeName")
    @Mapping(target = "loanStatus", source = "loanStatus")
    ViewLoanDTO loanToView(Loan source);


    @Mapping(target = "loanID", source = "loan.loanID")
    ViewUserLoanDTO userLoanToView(UserLoan source);

    @Mapping(target = "userLoanID", ignore = true)
    @Mapping(target = "loanStatus", ignore = true)
    @Mapping(target = "loanStartDate", ignore = true)
    @Mapping(target = "loanEndDate", ignore = true)
    UserLoan createUserLoanFromDTO(CreateUserLoanDTO createUserLoanDTO);




    @Mapping(target = "loanID",ignore = true)
    @Mapping(target = "loanType",ignore = true)
    @Mapping(target = "loanStatus", source = "loanStatus")
    Loan updateLoanFromDTO(UpdateLoanDTO source, @MappingTarget Loan loan);


    Loan createLoanFromDTO(CreateLoanDTO source);


}
