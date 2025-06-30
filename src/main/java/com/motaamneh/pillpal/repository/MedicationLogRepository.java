package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.MedicationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long> {
    List<MedicationLog> findByUser_IdAndDate(Long userId, LocalDate date);
    // List<MedicationLog> getAllLogsForUser(Long userId);
    boolean existsByScheduleIdAndDate(Long scheduleId, LocalDate date);
    boolean existsBySchedule_IdAndTakenAtBetween(Long scheduleId, LocalDateTime start, LocalDateTime end);

}
