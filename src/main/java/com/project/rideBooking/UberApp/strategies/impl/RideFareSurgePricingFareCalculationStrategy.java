package com.project.rideBooking.UberApp.strategies.impl;


import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.service.DistanceService;
import com.project.rideBooking.UberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;
    private static final double SURGE_FACTOR = 2;

    //surge factor can be based on weather (Rainy) price is doubled

    @Override
    public double calculateFare(RideRequest rideRequest) {

        double distance= distanceService.calculateDistance(rideRequest.getPickupLocation(),rideRequest.getDropOffLocation());

        return distance*RIDE_FARE_MULTIPLIER*SURGE_FACTOR;
    }
}
