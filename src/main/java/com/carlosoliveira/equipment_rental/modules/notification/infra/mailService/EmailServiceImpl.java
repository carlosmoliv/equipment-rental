package com.carlosoliveira.equipment_rental.modules.notification.infra.mailService;

import com.carlosoliveira.equipment_rental.modules.notification.application.ports.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String subject, String body) {
        try {
           SimpleMailMessage message = new SimpleMailMessage();
           message.setFrom(from);
           message.setTo(to);
           message.setSubject(subject);
           message.setText(body);
           mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Error sending email", e);
        }
    }
}
