package com.motaamneh.pillpal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationResponse {
    private Long id;
    private String name;
    private String dosage;
    private String description;
    private Long scheduleId;
    private List<ScheduleResponse> schedules;
}

