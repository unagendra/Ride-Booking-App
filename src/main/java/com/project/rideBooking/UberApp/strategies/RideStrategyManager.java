package com.project.rideBooking.UberApp.strategies;

import com.project.rideBooking.UberApp.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.project.rideBooking.UberApp.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.project.rideBooking.UberApp.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import com.project.rideBooking.UberApp.strategies.impl.RiderFareDefaultFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

   private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
   private  final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
   private  final RiderFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;
   private final RideFareSurgePricingFareCalculationStrategy  surgePricingFareCalculationStrategy;

    //If you are riderRating is good you will get a HighestRated Driver
    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){

        if (riderRating >= 4.8){
            return highestRatedDriverStrategy;
        } else {
            return nearestDriverStrategy;
        }

    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        //Based on Surge Time ( 6PM to 9PM ) Fare calculation strategy is allocated

        LocalTime surgeStartTime=LocalTime.of(18,0);
        LocalTime surgeEndTime=LocalTime.of(21,0);
        LocalTime currentTime=LocalTime.now();
        boolean isSurgeTime= currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if (isSurgeTime){
            return  surgePricingFareCalculationStrategy;
        } else {
            return defaultFareCalculationStrategy;
        }

    }
}
