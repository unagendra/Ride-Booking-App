package com.project.rideBooking.UberApp.strategies;

import com.project.rideBooking.UberApp.dto.RideRequestDTO;
import com.project.rideBooking.UberApp.entities.RideRequest;

public interface RideFareCalculationStrategy {
//whoever implementing this will have access to RIDE_FARE_MULTIPLIER.

    double  RIDE_FARE_MULTIPLIER = 10;

    double calculateFare(RideRequest rideRequest);
}
