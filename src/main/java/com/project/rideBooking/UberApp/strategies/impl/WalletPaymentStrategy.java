package com.project.rideBooking.UberApp.strategies.impl;

import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Payment;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.Rider;
import com.project.rideBooking.UberApp.entities.enums.PaymentStatus;
import com.project.rideBooking.UberApp.entities.enums.TransactionMethod;
import com.project.rideBooking.UberApp.repository.PaymentRepository;
import com.project.rideBooking.UberApp.service.PaymentService;
import com.project.rideBooking.UberApp.service.WalletService;
import com.project.rideBooking.UberApp.strategies.PaymentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


//Rider had 232, Driver had 500
//Ride cost is 100, commission = 30
//Rider -> 232-100 = 132
//Driver -> 500 + (100 - 30) = 570
@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        //Get the Driver from the Payment
        Driver driver=payment.getRide().getDriver();

        //Get the Rider from the Payment
        Rider rider=payment.getRide().getRider();

        //Deduct the Money from the Rider wallet based on the Payment(amount) for the Ride

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null,payment.getRide(), TransactionMethod.RIDE);

        //ADD Money to Diver wallet based on the Payment(amount) for the Ride after removing the PLATFORM_COMMISSION

        double driverCut= payment.getAmount() * (1-PLATFORM_COMMISSION);  //100*(1-0.7)-> 70 Rs

        walletService.addMoneyToWallet(driver.getUser(),driverCut,null,payment.getRide(),TransactionMethod.RIDE);

        //payment processed successfully, update the Payment Status to CONFIRMED

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);



    }
}
