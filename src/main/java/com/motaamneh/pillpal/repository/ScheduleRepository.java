package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUser_Id(Long userId);
}
