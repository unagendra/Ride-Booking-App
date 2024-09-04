package com.project.rideBooking.UberApp.repository;


import com.project.rideBooking.UberApp.entities.Payment;
import com.project.rideBooking.UberApp.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
}
