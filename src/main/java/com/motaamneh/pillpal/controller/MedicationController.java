package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @GetMapping("/user/{userId}")
    public List<Medication> getMedicationsByUser(@PathVariable Long userId) {
        return medicationService.getMedicationsByUserId(userId);

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
