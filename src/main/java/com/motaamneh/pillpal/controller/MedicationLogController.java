package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.entity.MedicationLog;
import com.motaamneh.pillpal.service.MedicationLogService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class MedicationLogController {
    private final MedicationLogService logService;


    @GetMapping("/user/{userId}/date/{date}")
    public List<MedicationLog> getLogsByUserAndDate(@PathVariable Long userId, @PathVariable String date) {
        return logService.getLogsByUserAndDate(userId, LocalDate.parse(date));
    }

    @PostMapping("/medication/{medicationId}")
    public MedicationLog logMedication(@PathVariable Long medicationId, @RequestBody MedicationLog log) {
        return logService.logMedication(medicationId, log);
    }

    @PutMapping("/{id}")
    public MedicationLog updateLog(@PathVariable Long id, @RequestBody MedicationLog log) {
        return logService.updateLog(id, log);
    }

//    @GetMapping("/user/{userId}")
//    public List<MedicationLog> getLogsByUser(@PathVariable Long userId) {
//        return logService.getAllLogsForUser(userId);
//    }


    @GetMapping("/did-log")
    public boolean didUserLogToday(
            @RequestParam Long scheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return logService.didUserLogToday(scheduleId, date);
    }

    @DeleteMapping("/{id}")
    public void deleteLog(@PathVariable Long id) {
        logService.deleteLog(id);
    }


}
