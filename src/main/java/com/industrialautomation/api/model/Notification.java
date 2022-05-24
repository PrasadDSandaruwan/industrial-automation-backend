package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String notification_name;

    @Column(nullable = false,unique = true)
    private String slug;

    private String sound_track;
}
