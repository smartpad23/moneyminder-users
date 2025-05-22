package com.moneyminder.moneyminderusers.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class UserEntity {
    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<GroupEntity> groups;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SessionEntity> sessions;

    @OneToMany(mappedBy = "requestingUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<GroupRequestEntity> requestingGroups;

    @OneToMany(mappedBy = "requestedUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<GroupRequestEntity> requestedGroups;
}
