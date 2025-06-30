package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.repository.MedicationLogRepository;
import com.motaamneh.pillpal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicationLogService {
    private final MedicationLogRepository medicationLogRepository;
    private final UserRepository userRepository;

    public MedicationLogService(MedicationLogRepository medicationLogRepository, UserRepository userRepository) {
        this.medicationLogRepository = medicationLogRepository;
        this.userRepository = userRepository;
    }


}
