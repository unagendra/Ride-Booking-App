package com.project.rideBooking.UberApp.repository;


import com.project.rideBooking.UberApp.entities.Rider;
import com.project.rideBooking.UberApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    //custom methods
    Optional<Rider> findByUser(User user);
}
