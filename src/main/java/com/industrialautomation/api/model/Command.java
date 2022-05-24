package com.industrialautomation.api.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "command")
@Getter
@Setter
public class Command {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String command;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "command_type_id", nullable = false)
    private CommandType commandType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;


}
