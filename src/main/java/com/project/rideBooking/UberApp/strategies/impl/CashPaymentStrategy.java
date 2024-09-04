package com.project.rideBooking.UberApp.strategies.impl;

import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Payment;
import com.project.rideBooking.UberApp.entities.Wallet;
import com.project.rideBooking.UberApp.entities.enums.PaymentStatus;
import com.project.rideBooking.UberApp.entities.enums.TransactionMethod;
import com.project.rideBooking.UberApp.repository.PaymentRepository;
import com.project.rideBooking.UberApp.service.PaymentService;
import com.project.rideBooking.UberApp.service.WalletService;
import com.project.rideBooking.UberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//Rider->100
//Driver->70
//PLATFORM_COMMISSION
//Deduct 30Rs from Driver wallet (100*30/100)= 30 (0.3)
@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        //get the current Driver from the payment
       Driver driver= payment.getRide().getDriver();

       //Get the Wallet of the Driver
        Wallet driverWallet=walletService.findByUser(driver.getUser());

        //Calculate the PLATFORM Commission from the Payment amount
        double PlatformCommission= payment.getAmount() * PLATFORM_COMMISSION;

        //deduct the PLATFORM Commission from the driver wallet (30Rs)
        walletService.deductMoneyFromWallet(driver.getUser(), PlatformCommission,null,payment.getRide(), TransactionMethod.RIDE);

        //payment processed successfully, update the Payment Status to CONFIRMED

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);


    }
}
