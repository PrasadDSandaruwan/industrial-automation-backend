package com.industrialautomation.api.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rates")
@Getter
@Setter @NoArgsConstructor
public class Rates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime rate_at;

    @Column(nullable = false)
    private Float rate;

    @Column(nullable = false)
    private Integer is_temp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    public Rates(Long id, LocalDateTime rate_at, Float rate, Integer is_temp, Machine machine) {
        this.id = id;
        this.rate_at = rate_at;
        this.rate = rate;
        this.is_temp = is_temp;
        this.machine = machine;
    }
}
