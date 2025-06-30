package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {
    private MedicationRepository medicationRepository;
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public  Medication createMedication(Medication medication) {
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
