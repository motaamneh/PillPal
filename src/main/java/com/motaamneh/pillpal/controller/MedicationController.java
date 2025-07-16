package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.dto.MedicationResponse;
import com.motaamneh.pillpal.dto.ScheduleResponse;
import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.service.MedicationLogService;
import com.motaamneh.pillpal.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;
    private final MedicationLogService medicationLogService;

    @GetMapping("/user/{userId}")
    public List<MedicationResponse> getMedicationsByUser(@PathVariable Long userId) {
        List<Medication> meds = medicationService.getMedicationsByUserId(userId);
        return meds.stream()
                .map(this::convertToMedicationResponse)
                .collect(Collectors.toList());
    }

    private MedicationResponse convertToMedicationResponse(Medication med) {
        List<ScheduleResponse> scheduleResponses = med.getSchedules().stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getId(),
                        schedule.getTime().toString(),
                        schedule.getRepeatType(),
                        schedule.getDaysOfWeek(),
                        schedule.getStartDate().toString(),
                        schedule.getEndDate().toString()
                )).collect(Collectors.toList());

        return new MedicationResponse(
                med.getId(),
                med.getName(),
                med.getDosage(),
                med.getDescription(),
                scheduleResponses
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
//    @GetMapping("/user/{userId}")
//    public List<Medication> getMedicationsByUser(
//            @PathVariable Long userId,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        if (date == null) date = LocalDate.now();
//
//        List<Medication> medications = medicationService.getMedicationsByUserId(userId);
//
//        for (Medication med : medications) {
//            boolean isTaken = medicationLogService.didUserLogToday(med.getId(), date);
//            med.setIsTakenToday(isTaken);
//        }
//
//        return medications;
//    }


}
