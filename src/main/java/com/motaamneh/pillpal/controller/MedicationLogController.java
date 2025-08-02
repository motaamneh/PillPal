package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.entity.MedicationLog;
import com.motaamneh.pillpal.entity.Schedule;
import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.repository.MedicationLogRepository;
import com.motaamneh.pillpal.repository.MedicationRepository;
import com.motaamneh.pillpal.repository.ScheduleRepository;
import com.motaamneh.pillpal.service.MedicationLogService;

import com.motaamneh.pillpal.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class MedicationLogController {
    private final MedicationLogService logService;
    private final MedicationRepository medicationRepository;
    private final ScheduleRepository scheduleRepository;
    private final MedicationLogRepository medicationLogRepository;


    @GetMapping("/user/{userId}/date/{date}")
    public List<MedicationLog> getLogsByUserAndDate(@PathVariable Long userId, @PathVariable String date) {
        return logService.getLogsByUserAndDate(userId, LocalDate.parse(date));
    }
    @GetMapping("/user/{userId}/today-taken")
    public List<Long> getTodayTakenMedications(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return medicationLogRepository.findByUser_IdAndDate(userId, date)
                .stream()
                .map(log -> log.getMedication().getId())
                .collect(Collectors.toList());
    }

    @PostMapping("/medication/{medicationId}")
    public ResponseEntity<?> logMedication(
            @PathVariable Long medicationId,
            @RequestBody Map<String, Object> payload) {

        try {
            // 1. Validate payload
            if (!payload.containsKey("date")) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", true, "message", "Date is required"));
            }

            LocalDate date = LocalDate.parse(payload.get("date").toString());

            // 2. Check for existing log
            if (medicationLogRepository.existsByMedication_IdAndDate(medicationId, date)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        Map.of("error", true, "message", "Medication already taken today"));
            }

            // 3. Find medication
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Medication not found"));

            // 4. Create new log
            MedicationLog log = new MedicationLog();
            log.setMedication(medication);
            log.setDate(date);
            log.setStatus("taken");
            log.setTakenAt(LocalDateTime.now());

            // 5. Handle user
            if (payload.containsKey("user")) {
                Long userId = Long.valueOf(((Map<?,?>)payload.get("user")).get("id").toString());
                User user = new User();
                user.setId(userId);
                log.setUser(user);
            }

            // 6. Handle schedule (optional)
            if (payload.containsKey("schedule")) {
                Long scheduleId = Long.valueOf(((Map<?,?>)payload.get("schedule")).get("id").toString());
                Schedule schedule = scheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Schedule not found"));
                log.setSchedule(schedule);
            }

            // 7. Save and return
            MedicationLog savedLog = medicationLogRepository.save(log);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "id", savedLog.getId(),
                            "medicationId", medicationId,
                            "date", date.toString(),
                            "takenAt", savedLog.getTakenAt().toString()
                    )
            ));

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", true, "message", "Invalid date format. Use yyyy-MM-dd"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", true, "message", "Server error: " + e.getMessage()));
        }
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
