package com.project.rideBooking.UberApp.strategies.impl;


import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.service.DistanceService;
import com.project.rideBooking.UberApp.strategies.RideFareCalculationStrategy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RiderFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

   private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        //Calculate the Fare based on distance b/w pickUpLocation and dropOffLocation
        //calculating the distance using DistanceServiceORSM Api
     double distance= distanceService.calculateDistance(rideRequest.getPickupLocation(),rideRequest.getDropOffLocation());

     //Fare=10 Rs/km (10*3km)
     return distance*RIDE_FARE_MULTIPLIER;

    }
}
