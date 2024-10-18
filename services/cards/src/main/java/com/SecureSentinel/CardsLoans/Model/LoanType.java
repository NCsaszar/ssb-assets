package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "loan_type")
public class LoanType {
    @Id
    @Column(name = "loan_type_id")
    private int loanTypeId;
    @Column(name = "loan_type_name")
    private String loanTypeName;

}
