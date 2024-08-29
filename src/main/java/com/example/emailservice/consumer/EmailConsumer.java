package com.example.emailservice.consumer;

import com.example.emailservice.dto.SendEmailMessage;
import com.example.emailservice.util.SendEmail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "send_email", groupId = "EmailService")
    public void processMessage(String message){
        try {
            SendEmailMessage sendEmailMessage = objectMapper.readValue(message, SendEmailMessage.class);
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");//smtp host
            prop.put("mail.smtp.port", "587"); //tls port
            prop.put("mail.smtp.auth", "true"); //enable authentication
            prop.put("mail.smtp.starttls.enable", "true"); //enable tls

            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("harry333b4u@gmail.com", "#######");
                }
            };
            Session session = Session.getInstance(prop, authenticator);
            SendEmail.sendEmail(session, sendEmailMessage.getTo(), sendEmailMessage.getSubject(), sendEmailMessage.getBody());
        }catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }
}
