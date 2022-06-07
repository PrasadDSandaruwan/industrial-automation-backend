package com.industrialautomation.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "user_types")
@Getter @Setter @NoArgsConstructor
public class UserType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String user_type_name;
    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "userType", fetch = FetchType.LAZY)
    private List<User> users;

    public UserType(long id) {
        this.id = id;
    } 
}
