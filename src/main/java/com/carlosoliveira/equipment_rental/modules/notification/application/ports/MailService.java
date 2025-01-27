package com.carlosoliveira.equipment_rental.modules.notification.application.ports;

public interface MailService {
    void send(String to, String subject, String body) throws Exception;
}
