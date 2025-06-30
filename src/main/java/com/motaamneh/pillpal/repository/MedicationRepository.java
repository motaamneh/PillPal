package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

}
