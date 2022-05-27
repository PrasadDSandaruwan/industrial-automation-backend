package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "command_type")
@Getter
@Setter
@NoArgsConstructor
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

    public CommandType(long id, String command_type_name, String slug) {
        this.id = id;
        this.command_type_name = command_type_name;
        this.slug = slug;
    }
}
