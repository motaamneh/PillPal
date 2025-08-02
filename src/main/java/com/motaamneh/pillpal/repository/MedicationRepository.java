package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    @Query("SELECT m FROM Medication m LEFT JOIN FETCH m.schedule WHERE m.user.id = :userId")
    List<Medication> findByUserIdWithSchedule(Long userId);
    List<Medication> findByUserId(Long userId);
}
