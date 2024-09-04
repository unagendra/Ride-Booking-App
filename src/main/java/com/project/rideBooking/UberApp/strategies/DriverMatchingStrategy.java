package com.project.rideBooking.UberApp.strategies;

import com.project.rideBooking.UberApp.dto.RideRequestDTO;
import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
