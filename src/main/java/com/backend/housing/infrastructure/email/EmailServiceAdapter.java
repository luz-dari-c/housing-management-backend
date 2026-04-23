package com.backend.housing.infrastructure.email;

import com.backend.housing.domain.ports.out.users.EmailServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceAdapter implements EmailServicePort {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña - Housing");
        message.setText(String.format("""
                Hola,
                
                Has solicitado restablecer tu contraseña.
                
                Tu código de verificación es: %s
                
                Este código expira en 5 minutos.
                
                Si no solicitaste este cambio, ignora este mensaje.
                
                Saludos,
                Equipo Housing
                """, code));

        mailSender.send(message);
    }
}