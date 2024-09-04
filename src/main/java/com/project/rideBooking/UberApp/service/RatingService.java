package com.project.rideBooking.UberApp.service;


import com.project.rideBooking.UberApp.dto.DriverDto;
import com.project.rideBooking.UberApp.dto.RiderDto;
import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.Rider;

public interface RatingService {
    DriverDto rateDriver(Ride ride, Integer rating);

    RiderDto rateRider(Ride ride, Integer rating);

    //creating new Rating object for every ride
    void createNewRating(Ride ride);


}
