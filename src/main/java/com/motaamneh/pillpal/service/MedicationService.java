package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.dto.MedicationResponse;
import com.motaamneh.pillpal.dto.ScheduleResponse;
import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.repository.MedicationRepository;
import com.motaamneh.pillpal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    private final UserRepository userRepository;
    public MedicationService(MedicationRepository medicationRepository, UserRepository userRepository) {
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
    }

    public Medication createMedication(Long userId, Medication medication) {
        if (medication.getName() == null || medication.getName().isEmpty()) {
            throw new IllegalArgumentException("Medication name cannot be empty");
        }
        medication.setDescription(Optional.ofNullable(medication.getDescription()).orElse("No description"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        medication.setUser(user); // associate user
        return medicationRepository.save(medication);
    }
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
//    public List<Medication> getMedicationsByUserId(Long userId) {
//        return medicationRepository.findByUserId(userId);
//    }


    public Optional<Medication> getUserMedicationById(Long id) {
       return medicationRepository.findById(id);
    }

    public Medication updateMedication(Long id,Medication updatedMedication) {
        return medicationRepository.findById(id)
                .map( medication -> {
                    medication.setName(updatedMedication.getName());
                    medication.setDosage(updatedMedication.getDosage());
                    medication.setDescription(updatedMedication.getDescription());
                    medication.setUser(updatedMedication.getUser());

                    return medicationRepository.save(medication);
                        }
                ).orElseThrow(()-> new RuntimeException("Medication is not found"));
    }
    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }

    // In MedicationService
    public List<Medication> getMedicationsByUserId(Long userId) {
        List<Medication> meds = medicationRepository.findByUserId(userId);
        // Initialize schedule if needed
        meds.forEach(med -> {
            if (med.getSchedule() != null) {
                med.getSchedule().getId(); // Triggers lazy loading
            }
        });
        return meds;
    }
    private MedicationResponse convertToMedicationResponse(Medication med) {
        List<ScheduleResponse> schedules = med.getSchedules().stream()
                .map(s -> new ScheduleResponse(
                        s.getId(),
                        s.getTime().toString(),
                        s.getRepeatType(),
                        s.getDaysOfWeek(),
                        s.getStartDate().toString(),
                        s.getEndDate().toString()
                )).toList();

        return new MedicationResponse(
                med.getId(),
                med.getName(),
                med.getDosage(),
                med.getDescription(),
                med.getScheduleId(),
                schedules
        );
    }


}
