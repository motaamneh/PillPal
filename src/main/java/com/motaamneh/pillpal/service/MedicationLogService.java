package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.Medication;
import com.motaamneh.pillpal.entity.MedicationLog;
import com.motaamneh.pillpal.repository.MedicationLogRepository;
import com.motaamneh.pillpal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicationLogService {
    private final MedicationLogRepository medicationLogRepository;

    public MedicationLogService(MedicationLogRepository medicationLogRepository, UserRepository userRepository) {
        this.medicationLogRepository = medicationLogRepository;

    }
    public MedicationLog logMedication(MedicationLog medication) {
        return medicationLogRepository.save(medication);
    }
    public List<MedicationLog> getLogsByUserAndDate(Long userId, LocalDate date) {
        return medicationLogRepository.findByUserAndDate(userId,date);
    }
    public List<MedicationLog> getAllLogsForUser(Long userId) {
        return medicationLogRepository.findByUserId(userId);
    }
    public boolean didUserLogToday(Long scheduleId, LocalDate date) {
        return medicationLogRepository.existsByScheduleIdAndDate(scheduleId,date);
    }
    public void deleteLog(Long id){
        medicationLogRepository.deleteById(id);
    }


}
