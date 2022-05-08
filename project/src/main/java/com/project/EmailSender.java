package com.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class EmailSender {
    //https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/integration.html#mail
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String email) throws MessagingException, IOException {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Account Created");
        msg.setText("You have successfully created your account on our website!");

        javaMailSender.send(msg);

    }
}
