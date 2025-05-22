package com.moneyminder.moneyminderusers.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "GROUP_REQUESTS")
public class GroupRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", columnDefinition = "CHAR(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTING_USERNAME", nullable = false)
    private UserEntity requestingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTED_USERNAME", nullable = false)
    private UserEntity requestedUser;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "RESOLVED_DATE")
    private LocalDate resolvedDate;

    @Column(name = "ACCEPTED")
    private Boolean accepted;
}
