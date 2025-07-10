package com.motaamneh.pillpal.service;


import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to PillPal");
        message.setText("Hello " + name + "\n\nThank you for using PillPal!\n\n Regards, \nPillPal Team!");
        mailSender.send(message);
    }
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Verify Your Email");
            message.setText("Your OTP: " + otp+"\n\nUse this to proceed with verifying your email.\n\nPillPal Team!");

            logger.info("Attempting to send email to: {}", toEmail);
            //logger.info("Using SMTP host: {}", mailSender.getJavaMailProperties().get("mail.smtp.host"));

            mailSender.send(message);
            logger.info("Email apparently sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("FAILED to send email to {}: {}", toEmail, e.getMessage());
            throw e;
        }
    }
    public void sendResetOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for resetting your password is " + otp + "\n\nUse this to proceed with resetting your password.\n\nPillPal Team!");
        mailSender.send(message);
    }


    public void sendOtpEmailWithRetry(String toEmail, String otp, int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                sendOtpEmail(toEmail, otp);
                return;
            } catch (Exception e) {
                attempt++;
                logger.warn("Email send attempt {} failed for {}", attempt, toEmail, e);
                if (attempt >= maxRetries) {
                    throw new RuntimeException("Failed to send email after " + maxRetries + " attempts");
                }
                try {
                    Thread.sleep(2000); // Wait 2 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Email sending interrupted", ie);
                }
            }
        }
    }

}
