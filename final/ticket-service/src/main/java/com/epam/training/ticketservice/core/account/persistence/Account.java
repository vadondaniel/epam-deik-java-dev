package com.epam.training.ticketservice.core.account.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.EnumType;

@Entity
@Table(name = "Accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Account(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public enum Role {
        ADMIN,
        USER
    }
}
