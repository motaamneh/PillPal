package com.motaamneh.pillpal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {
    private Long id;
    private String time;
    private String repeatType;
    private String daysOfWeek;
    private String startDate;
    private String endDate;
}

