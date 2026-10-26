package com.scaler.userauthservice.model;

import com.scaler.userauthservice.model.Enum.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
