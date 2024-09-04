package com.project.rideBooking.UberApp.strategies.impl;



import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.repository.DriverRepository;
import com.project.rideBooking.UberApp.strategies.DriverMatchingStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private  final DriverRepository driverRepository;


    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearByTopRatedDriver(rideRequest.getPickupLocation());
    }
}

