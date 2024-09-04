package com.project.rideBooking.UberApp.service;


import com.project.rideBooking.UberApp.entities.Payment;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
