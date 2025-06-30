package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.entity.Schedule;
import com.motaamneh.pillpal.service.ScheduleService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/medication/{medicationId}")
    public Schedule createSchedule(@PathVariable Long medicationId, @RequestBody Schedule schedule) {
        return scheduleService.addSchedule(medicationId, schedule);
    }

    @PutMapping("/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(id, schedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

}
