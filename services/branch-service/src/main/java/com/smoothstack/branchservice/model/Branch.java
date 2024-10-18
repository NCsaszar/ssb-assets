package com.smoothstack.branchservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
@Entity
@Table(name = "branch", indexes = {@Index(name = "idx_branch_id", columnList = "branch_id", unique = true)})
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id", unique = true)
    private Integer branchId;

    @Column(name = "branch_code")  // search by this instead of branchId for security
    private String branchCode;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "branch_manager")
    private Integer branchManager;  // Integer for admin_id, or String for name?

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "date_modified")
    private Timestamp dateModified;

    @PrePersist
    protected void onCreate() {
        dateCreated = new Timestamp(System.currentTimeMillis());
        dateModified = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        dateModified = new Timestamp(System.currentTimeMillis());
    }

    @OneToMany(
            mappedBy = "branch",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Banker> bankers = new ArrayList<>();

    @OneToMany(
            mappedBy = "branch",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(
            mappedBy = "branch",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Queue> queues = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "branch_service_type",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<ServiceType> serviceTypes = new ArrayList<>();
}