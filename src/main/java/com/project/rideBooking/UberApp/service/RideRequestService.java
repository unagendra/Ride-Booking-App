package com.project.rideBooking.UberApp.service;


import com.project.rideBooking.UberApp.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
