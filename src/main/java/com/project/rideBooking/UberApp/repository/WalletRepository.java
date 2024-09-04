package com.project.rideBooking.UberApp.repository;


import com.project.rideBooking.UberApp.entities.User;
import com.project.rideBooking.UberApp.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}
