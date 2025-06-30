package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.entity.MedicationLog;
import com.motaamneh.pillpal.repository.MedicationLogRepository;
import com.motaamneh.pillpal.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicationLogService {
    private final MedicationLogRepository medicationLogRepository;
    private final MedicationRepository medicationRepository;


    public MedicationLogService(MedicationLogRepository medicationLogRepository, MedicationRepository medicationRepository) {
        this.medicationLogRepository = medicationLogRepository;
        this.medicationRepository = medicationRepository;

    }
    // Create a new log and attach it to a medication
    public MedicationLog logMedication(Long medicationId, MedicationLog log) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        log.setMedication(medication);
        return medicationLogRepository.save(log);
    }
    public List<MedicationLog> getLogsByUserAndDate(Long userId, LocalDate date) {
        return medicationLogRepository.findByUser_IdAndDate(userId,date);
    }
//    public List<MedicationLog> getAllLogsForUser(Long userId) {
//        return medicationLogRepository.(userId);
//    }
    public boolean didUserLogToday(Long scheduleId, LocalDate date) {
        return medicationLogRepository.existsByScheduleIdAndDate(scheduleId,date);
    }
    public MedicationLog updateLog(Long id, MedicationLog updatedLog) {
        MedicationLog existing = medicationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));

        existing.setTakenAt(updatedLog.getTakenAt());
        existing.setStatus(updatedLog.getStatus());

        return medicationLogRepository.save(existing);
    }
    public void deleteLog(Long id){
        medicationLogRepository.deleteById(id);
    }


//    public List<MedicationLog> getAllLogsForUser(Long userId) {
//        return medicationLogRepository.getAllLogsForUser(userId);
//    }
}
