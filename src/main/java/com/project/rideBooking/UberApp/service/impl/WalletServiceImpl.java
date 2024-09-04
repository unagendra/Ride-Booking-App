package com.project.rideBooking.UberApp.service.impl;

import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.User;
import com.project.rideBooking.UberApp.entities.Wallet;
import com.project.rideBooking.UberApp.entities.WalletTransaction;
import com.project.rideBooking.UberApp.entities.enums.TransactionMethod;
import com.project.rideBooking.UberApp.entities.enums.TransactionType;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.repository.WalletRepository;
import com.project.rideBooking.UberApp.service.WalletService;
import com.project.rideBooking.UberApp.service.WalletTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private  final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        //check wallet is associated with the user
        Wallet wallet= findByUser(user);   //DRY
        //Add money to the wallet
        wallet.setBalance(wallet.getBalance() + amount);

        //Create a new createNewWalletTransaction

        WalletTransaction walletTransaction=WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)                //updated wallet is included in wallet transaction
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        //check wallet is associated with user
        Wallet wallet=findByUser(user);

        //deduct money from the wallet
        wallet.setBalance(wallet.getBalance() - amount);

        //Create a new createNewWalletTransaction

        WalletTransaction walletTransaction=WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)                //updated wallet is included in wallet transaction
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

       // walletTransactionService.createNewWalletTransaction(walletTransaction);

        wallet.getTransactions().add(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {


    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: "+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        //create new wallet and add user to it
        Wallet wallet=new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("wallet not found for the user with the id "+user.getId()));
    }
}
