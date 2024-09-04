package com.project.rideBooking.UberApp.service.impl;

import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.repository.RideRequestRepository;
import com.project.rideBooking.UberApp.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

   private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId).orElseThrow(
                ()-> new ResourceNotFoundException("Ride request with id not Found "+ rideRequestId));


    }

    @Override
    public void update(RideRequest rideRequest) {
        //check if the ID is null
        rideRequestRepository.findById(rideRequest.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("Ride request with id not Found "+ rideRequest.getId()));

        rideRequestRepository.save(rideRequest);
    }
}
