package com.project.rideBooking.UberApp.strategies;

import com.project.rideBooking.UberApp.entities.enums.PaymentMethod;
import com.project.rideBooking.UberApp.strategies.impl.CashPaymentStrategy;
import com.project.rideBooking.UberApp.strategies.impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {
    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
                                            //new way of writing switch statement
       return switch (paymentMethod){
            case CASH ->cashPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
        };
    }
}
