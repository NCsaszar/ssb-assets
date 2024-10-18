package com.smoothstack.branchservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
@Entity
@Table(name = "appointment", indexes = {@Index(name = "idx_appointment_id", columnList = "appointment_id", unique = true)})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "banker_id")
    private Integer bankerId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "timeslot")
    private LocalDateTime timeslot;

    @Column(name = "description")
    private String description;

    @Version
    private Integer version;

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

    @ManyToOne(fetch = FetchType.LAZY)// , cascade = CascadeType.ALL)  // consider cascade type
    @JoinColumn(
            name = "branch_id",
            referencedColumnName = "branch_id",
            insertable = false, updatable = false
    )
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "banker_id",
            referencedColumnName = "banker_id",
            insertable = false, updatable = false
    )
    private Banker banker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "service_id",
            referencedColumnName = "service_id",
            insertable = false, updatable = false
    )
    private ServiceType serviceType;
}