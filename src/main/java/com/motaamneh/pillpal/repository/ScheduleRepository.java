package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
