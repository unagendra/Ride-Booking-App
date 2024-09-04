package com.project.rideBooking.UberApp.repository;

import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Rating;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRider(Rider rider); //All the Ratings of the Rider
    List<Rating> findByDriver(Driver driver); //All the Ratings of the Driver

    //Find the Rating for the Ride
    Optional<Rating> findByRide(Ride ride);

}
