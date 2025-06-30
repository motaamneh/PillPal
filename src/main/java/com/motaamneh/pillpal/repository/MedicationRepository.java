package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {


    List<Medication> findByUserId(Long userId);
}
