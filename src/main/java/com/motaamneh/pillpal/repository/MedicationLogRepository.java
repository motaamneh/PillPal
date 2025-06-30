package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.MedicationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long> {
    List<MedicationLog> findByUserAndDate(Long userId, LocalDate date);

    List<MedicationLog> findByUserId(Long userId);

    boolean existsByScheduleIdAndDate(Long scheduleId, LocalDate date);
}
