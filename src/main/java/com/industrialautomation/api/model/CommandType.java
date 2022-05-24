package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "command")
@Getter
@Setter
public class CommandType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String command_type_name;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "commandType", fetch = FetchType.LAZY)
    private List<Command> commands;


}
