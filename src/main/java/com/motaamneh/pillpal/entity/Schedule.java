package com.motaamneh.pillpal.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime time;
    private String repeatType;
    private String daysOfWeek;
    private LocalDate startDate;
    private LocalDate endDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;


}
