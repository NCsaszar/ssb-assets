package com.smoothstack.branchservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name = "queue", indexes = {@Index(name = "idx_queue_id", columnList = "queue_id", unique = true)})
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Integer queueId;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "checkin_time")
    private Timestamp checkinTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "branch_id",
            referencedColumnName = "branch_id",
            insertable = false, updatable = false
    )
    private Branch branch;

    @PrePersist
    protected void onCreate() {
        checkinTime = new Timestamp(System.currentTimeMillis());
    }

}