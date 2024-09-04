package com.project.rideBooking.UberApp.strategies;


import com.project.rideBooking.UberApp.entities.Payment;

public interface PaymentStrategy {
    //amount deducted from the driver wallet after every ride
    Double PLATFORM_COMMISSION = 0.3;

    //Process payment using different payment methods
    void processPayment(Payment payment);
}
