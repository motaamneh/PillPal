package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.io.ProfileRequest;
import com.motaamneh.pillpal.io.ProfileResponse;
import com.motaamneh.pillpal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        User newProfile = convertToUser(request);
        if (!userRepository.existsByEmail(request.getEmail())) {

            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");

    }

    @Override
    public ProfileResponse getProfile(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"+email));

        return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {
        User existingUser = userRepository.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("User not found"+email));

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        long expiryTime = System.currentTimeMillis()+(15*60*1000);

        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expiryTime);
        userRepository.save(existingUser);

        try{

            emailService.sendResetOtpEmail(existingUser.getEmail(), otp);
        }catch (Exception e){
            throw new RuntimeException("Failed to send reset otp");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"+email));
        if(existingUser.getResetOtp() == null|| !existingUser.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        if(existingUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP Expired");
        }
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0L);
        userRepository.save(existingUser);

    }

    // ProfileServiceImpl.java
    @Override
    @Transactional
    public void sendOtp(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Properly handle Boolean object with null safety
        if (existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()) {
            logger.info("Account already verified for email: {}", email);
            return;
        }

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        long expiryTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24 hours
        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);
        userRepository.save(existingUser);
        logger.info("Generated OTP for user: {}", email);

        try {
            emailService.sendOtpEmailWithRetry(existingUser.getEmail(), otp, 3);
            logger.info("Verification OTP sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send OTP to: {} - {}", email, e.getMessage());
            throw new RuntimeException("Failed to send verification email");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
        if(existingUser.getVerifyOtp() ==null || !existingUser.getVerifyOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");

        }
        if (existingUser.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP Expired");
        }
        existingUser.setIsAccountVerified(true);
        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpireAt(0L);
        userRepository.save(existingUser);



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
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }
}
