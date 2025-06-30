package com.motaamneh.pillpal.service;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        medication.setUser(user); // associate user
        return medicationRepository.save(medication);
    }
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
    public List<Medication> getMedicationsByUserId(Long userId) {
        return medicationRepository.findByUserId(userId);
    }


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

}
