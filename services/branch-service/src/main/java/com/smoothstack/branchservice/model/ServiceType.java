package com.smoothstack.branchservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name = "service_type", indexes = {@Index(name = "idx_service_id", columnList = "service_id", unique = true)})
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "service_type_name")
    private String serviceTypeName;

    @Column(name = "description")
    private String description;

    @OneToMany(
            mappedBy = "serviceType",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToMany(mappedBy = "serviceTypes", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Branch> branches = new ArrayList<>();

}