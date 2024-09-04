package com.project.rideBooking.UberApp.service.impl;

import com.project.rideBooking.UberApp.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        try{
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            javaMailSender.send(simpleMailMessage);
            log.info("Email sent successfully");
        } catch (Exception e){
            log.info("Cannot send Email "+e.getMessage());
        }

    }

    @Override
    public void sendEmail(String[] toEmail, String subject, String body) {
        try{
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setTo("nagendarup95@gmail.com");
            simpleMailMessage.setBcc(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            javaMailSender.send(simpleMailMessage);
            log.info("Email sent successfully");
        } catch (Exception e){
            log.info("Cannot send Email "+e.getMessage());
        }
    }
}
