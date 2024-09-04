package com.project.rideBooking.UberApp.service.impl;


import com.project.rideBooking.UberApp.dto.RideRequestDTO;
import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.entities.Rider;
import com.project.rideBooking.UberApp.entities.enums.RideRequestStatus;
import com.project.rideBooking.UberApp.entities.enums.RideStatus;
import com.project.rideBooking.UberApp.repository.RideRepository;
import com.project.rideBooking.UberApp.service.RideRequestService;
import com.project.rideBooking.UberApp.service.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private  final ModelMapper mapper;
    private  final RideRequestService rideRequestService;
    private  final RideRepository rideRepository;


    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(()->new RuntimeException("Ride not Found with this Id: "+rideId));
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        //set the status of the rideRequest to CONFIRMED
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        //Convert the rideRequest to ride using Model Mapper(Many fields are common we can reuse them)
        Ride ride=mapper.map(rideRequest,Ride.class);

        //Driver is Confirming the ride.
        ride.setRideStatus(RideStatus.CONFIRMED);

        //Driver information needs to passed to Driver
        ride.setDriver(driver);

        //Generate otp(4 digit no) and add it to the Ride
        ride.setOtp(generateRandomOTP());

        //Id might be copied while Converting rideRequest to ride, set it to null as it will be auto generated at DB level
        ride.setId(null);

        //update the rideRequest and send the response back
        rideRequestService.update(rideRequest);

        //Save the ride to DB and return the ride
         return   rideRepository.save(ride);

    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
       ride.setRideStatus(rideStatus);
      return rideRepository.save(ride);

    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }


    @Override
    public String generateRandomOTP() {

        //Create object of the Random class
        Random random=new Random();

        //use it and call the nextInt method with the bound value
        int otpInt= random.nextInt(10000);  //0->9999

        //Convert the otpInt into String and format it using String.Format()
        //convert the 1,2,3 digit random numbers into 4 digit number by appending 0s in the beginning.

        return String.format("%04d",otpInt);

    }

}


