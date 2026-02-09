package com.cantech.projects.airBnbApp.entities;

import com.cantech.projects.airBnbApp.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; //encode it and store it

    @Column(nullable = true)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)   //This is for the hibernate that will create a separate table for it
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;




}
