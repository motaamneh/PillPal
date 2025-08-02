package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.dto.MedicationResponse;
import com.motaamneh.pillpal.dto.ScheduleResponse;
import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.entity.Schedule;
import com.motaamneh.pillpal.service.MedicationLogService;
import com.motaamneh.pillpal.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;
    private final MedicationLogService medicationLogService;

    //@GetMapping("/user/{userId}")
    @GetMapping("/user/{userId}")
    public List<MedicationResponse> getMedicationsByUser(@PathVariable Long userId) {
        List<Medication> meds = medicationService.getMedicationsByUserId(userId);
        return meds.stream()
                .map(this::convertToMedicationResponse)
                .collect(Collectors.toList());
    }

    private MedicationResponse convertToMedicationResponse(Medication med) {
        // Handle null schedules gracefully
        List<ScheduleResponse> scheduleResponses = Optional.ofNullable(med.getSchedules())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertToScheduleResponse)
                .collect(Collectors.toList());

        return new MedicationResponse(
                med.getId(),
                med.getName(),
                med.getDosage(),
                med.getDescription(),
                med.getScheduleId(), // This now uses the getter we added
                scheduleResponses
        );
    }

    private ScheduleResponse convertToScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTime() != null ? schedule.getTime().toString() : null,
                schedule.getRepeatType(),
                schedule.getDaysOfWeek(),
                schedule.getStartDate() != null ? schedule.getStartDate().toString() : null,
                schedule.getEndDate() != null ? schedule.getEndDate().toString() : null
        );
    }

    @PostMapping("/user/{userId}")
    public Medication createMedication(@PathVariable Long userId, @RequestBody Medication medication) {
        return medicationService.createMedication(userId,medication);
    }
    @PutMapping("/{id}")
    public Medication updateMedication(@PathVariable Long id, @RequestBody Medication medication) {
        return medicationService.updateMedication(id,medication);
    }
    @DeleteMapping("/{id}")
    public void deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
    }



}
