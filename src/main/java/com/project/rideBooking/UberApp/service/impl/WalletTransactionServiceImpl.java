package com.project.rideBooking.UberApp.service.impl;

import com.project.rideBooking.UberApp.entities.WalletTransaction;
import com.project.rideBooking.UberApp.repository.WalletTransactionRepository;
import com.project.rideBooking.UberApp.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {
    private final WalletTransactionRepository walletTransactionRepository;
    private  final ModelMapper mapper;

    /**
     * Everything happening in wallet (add money/deduct money) is associated with a transaction,
     * create an instance of walletTransaction and save it to database  table.
     * @param walletTransaction
     */

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
      // WalletTransaction walletTransaction= mapper.map(walletTransaction, WalletTransaction.class);
       walletTransactionRepository.save(walletTransaction);
    }
}
