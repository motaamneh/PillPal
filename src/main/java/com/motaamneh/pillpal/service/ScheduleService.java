package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.Schedule;
import com.motaamneh.pillpal.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;

    }
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
    public Schedule updateSchedule(Long id,Schedule updatedSchedule) {
        return scheduleRepository.findById(id)
                .map(schedule -> {
                    schedule.setTime(updatedSchedule.getTime());
                    schedule.setRepeatType(updatedSchedule.getRepeatType());
                    schedule.setDaysOfWeek(updatedSchedule.getDaysOfWeek());
                    schedule.setStartDate(updatedSchedule.getStartDate());
                    schedule.setEndDate(updatedSchedule.getEndDate());
                    schedule.setMedication(updatedSchedule.getMedication());
                    return scheduleRepository.save(schedule);

                }).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    public List<Schedule> getSchedulesByUser(Long id) {
        return scheduleRepository.findByUserId(id);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
