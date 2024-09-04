package com.project.rideBooking.UberApp.strategies.impl;

import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.repository.DriverRepository;
import com.project.rideBooking.UberApp.strategies.DriverMatchingStrategy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private  final DriverRepository driverRepository;
    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        //Define custom query to find the Matching Driver in the Driver Repo(using the nearest to the pickUpLocation strategy)
        return driverRepository.findTenNearestDriver(rideRequest.getPickupLocation());
    }
}
