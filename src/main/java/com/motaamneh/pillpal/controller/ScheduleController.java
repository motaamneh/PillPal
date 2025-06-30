package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.entity.Schedule;
import com.motaamneh.pillpal.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/medication/{medicationId}")
    public Optional<Schedule> getSchedulesByMedication(@PathVariable Long medicationId) {
        return scheduleService.getScheduleById(medicationId);
    }

    @PostMapping(  value = "/medication/{medicationId}"
            )
    public Schedule createSchedule(@PathVariable Long medicationId, @RequestBody Schedule schedule) {
        return scheduleService.addSchedule(medicationId, schedule);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule updatedSchedule) {
        return scheduleService.updateSchedule(id, updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

}
