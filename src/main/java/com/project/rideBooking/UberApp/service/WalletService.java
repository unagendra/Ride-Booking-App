package com.project.rideBooking.UberApp.service;


import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.User;
import com.project.rideBooking.UberApp.entities.Wallet;
import com.project.rideBooking.UberApp.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);


    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    //Take user(Driver/Rider) and return Driver/Rider wallet
    Wallet findByUser(User user);

}
