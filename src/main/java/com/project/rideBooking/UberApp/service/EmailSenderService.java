package com.project.rideBooking.UberApp.service;

public interface EmailSenderService {
    public void sendEmail(String toEmail, String subject, String body);

    //multiple users
    public void sendEmail(String toEmail[], String subject, String body);
}
