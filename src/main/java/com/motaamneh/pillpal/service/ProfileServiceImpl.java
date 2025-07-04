package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.io.ProfileRequest;
import com.motaamneh.pillpal.io.ProfileResponse;
import com.motaamneh.pillpal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;


    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        User newProfile = convertToUser(request);
        if (!userRepository.existsByEmail(request.getEmail())) {

            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");

    }

    private ProfileResponse convertToProfileResponse(User newProfile) {
        return ProfileResponse.builder()
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .userId(newProfile.getUserId())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private User convertToUser(ProfileRequest request) {
        return User.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(request.getPassword())
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }
}
