package com.industrialautomation.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String password;

    //This represents user type.
    //It can be either 'USER', or 'ADMIN' in our application.
    @Column(nullable = false)
    private String role;

}
