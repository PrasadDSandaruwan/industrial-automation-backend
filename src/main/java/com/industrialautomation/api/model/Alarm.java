package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "alarm")
@Getter @Setter @NoArgsConstructor
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alarm_name;

    @Column(nullable = false,unique = true)
    private String slug;

    @Column(nullable = false)
    private LocalDateTime added_at;

    private LocalDateTime deleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nearest_machine_id", nullable = false)
    private Machine machine;

}
