package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.MedicationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long> {
}
