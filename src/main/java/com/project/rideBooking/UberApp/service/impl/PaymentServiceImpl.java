package com.project.rideBooking.UberApp.service.impl;

import com.project.rideBooking.UberApp.entities.Payment;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.enums.PaymentStatus;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.repository.PaymentRepository;
import com.project.rideBooking.UberApp.service.PaymentService;
import com.project.rideBooking.UberApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {

        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride with id: "+ride.getId()));

        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod())  //CASHPayment || WalletPayment Strategy
                .processPayment(payment);

    }

    /**
     * Creating Payment object from the Ride and Saving it to Database Table
     * Initially, PaymentStatus.PENDING when you  create New Payment
     * Then based on Payment method , processPayment method of corresponding strategy is called
     * then once payment is processed, it is updated as PaymentStatus.CONFIRMED
     * @param ride
     * @return Payment payment
     */
    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment=Payment.builder()
                .ride(ride)
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
