package com.moneyminder.moneyminderusers.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "SESSIONS")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "TOKEN", length = 2048, nullable = false)
    private String token;

    @Column(name = "CREATION_DATE")
    private LocalDate creationDate;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME", nullable = false)
    private UserEntity user;
}
