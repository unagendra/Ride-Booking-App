package com.project.rideBooking.UberApp.service.impl;


import com.project.rideBooking.UberApp.dto.DriverDto;
import com.project.rideBooking.UberApp.dto.RideDto;
import com.project.rideBooking.UberApp.dto.RiderDto;
import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.RideRequest;
import com.project.rideBooking.UberApp.entities.User;
import com.project.rideBooking.UberApp.entities.enums.RideRequestStatus;
import com.project.rideBooking.UberApp.entities.enums.RideStatus;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.repository.DriverRepository;
import com.project.rideBooking.UberApp.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

   private final RideRequestService rideRequestService;
   private final DriverRepository driverRepository;
   private final RideService rideService;
   private  final ModelMapper mapper;
   private final PaymentService paymentService;
   private final RatingService ratingService;

    @Override
    public RideDto acceptRide(Long rideRequestId) {
        //get the rideRequest using rideRequestService
        RideRequest rideRequest=rideRequestService.findRideRequestById(rideRequestId);

        //Check rideRequest status is Pending(Driver can only acceptRide is status is PENDING)
        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw  new RuntimeException("RideRequest cannot be accepted, status is" + rideRequest.getRideRequestStatus());
        }

        //get Current Driver
        Driver currentDriver=getCurrentDriver();

        //check if the currentDriver is available
        if (!currentDriver.getAvailable()){
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }

        //Set the Driver Availability to false(This Driver has accepted ride hence he is not available for other rideRequest)

       Driver savedDriver= updateDriverAvailability(currentDriver, false);


        //create newRide using rideService
       Ride ride= rideService.createNewRide(rideRequest,savedDriver);

        return mapper.map(ride,RideDto.class);
    }

    /**
     * Driver can cancel the ride, only when the ride has not started yet(accidental accepted ride).
     * When ride is ONGOING status, Driver cannot cancel the Ride, He can only End the Ride.
     * Driver can only cancel the ride when RideStatus is CONFIRMED
     * @param rideId
     * @return RideDto
     */
    @Override
    public RideDto cancelRide(Long rideId) {
        //get the ride using rideService
        Ride ride=rideService.getRideById(rideId);

        //get the current Driver of the ride
        Driver driver=getCurrentDriver();

        //Check if the driver really is the one who ones the ride(accepted the ride)
        if (!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        //check if the RideStatus of the Rise is CONFIRMED
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw  new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        //cancel logic-> Driver will cancel the ride by updating RideStatus of the ride to CANCELLED
        rideService.updateRideStatus(ride,RideStatus.CANCELLED);

        //Make the Driver available again, He is free to accept other rides
        updateDriverAvailability(driver, true);


        return mapper.map(ride,RideDto.class);
    }

    @Override
    public RideDto startRide(Long rideId, String otp) {
        //get the ride details based on rideRequestId
        Ride ride= rideService.getRideById(rideId);

        //get the current Driver of the ride
        Driver driver=getCurrentDriver();

        //Check if the driver really is the one who ones the ride(accepted the ride)
        if (!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        //Check if the rideStatus is CONFIRMED, Driver can only start the ride if rideStatus is CONFIRMED

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride status is not CONFIRMED hence cannot be started, status: "+ride.getRideStatus());
        }

        //Verify the OTP Received by the rider for the ride

        if (!otp.equals(ride.getOtp())){
            throw new RuntimeException("Otp is not valid, otp: "+otp);
        }

        //Update the start Time of the ride and update the RideStatus as ONGOING and save it to Database
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ONGOING);

        //create Payment object for the savedRide(which will give Payment amount,Payment method)
        paymentService.createNewPayment(savedRide);

        //Create New Rating object for the Saved Ride
        ratingService.createNewRating(savedRide);

        return  mapper.map(savedRide,RideDto.class);
    }

    @Override
    public RideDto endRide(Long rideId) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        //Check if the rideStatus is ONGOING, Driver can only end the ride if rideStatus is ONGOING
        if(!ride.getRideStatus().equals(RideStatus.ONGOING)) {
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: "+ride.getRideStatus());
        }

        //update the ride ended at (end time)
        ride.setEndedAt(LocalDateTime.now());
        //Driver, Update the RideStatus as ENDED
        Ride savedRide=rideService.updateRideStatus(ride, RideStatus.ENDED);

        //Driver is now available for other rides
        updateDriverAvailability(driver, true);

        //Process Payment by passing the ride
        paymentService.processPayment(ride);


        return mapper.map(savedRide,RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
      //get the Ride using RideId
        Ride ride=rideService.getRideById(rideId);

        //get the Current Driver
        Driver driver=getCurrentDriver();

        //check whether Driver is owner of the ride
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not the owner of this Ride");
        }

        //Check whether Ride status is Ended, Rating can be given only if the status is Ended
        if (!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: "+ride.getRideStatus());
        }

     return   ratingService.rateRider(ride, rating);

    }

    /**
     * USed to Get Driver information
     * @return
     */
    @Override
    public DriverDto getMyProfile() {
        //get current Driver
        Driver driver=getCurrentDriver();

        return mapper.map(driver,DriverDto.class);
    }

    /**
     * Returns all the Ride, of the Driver.
     * Driver can have 1000 of ride, we don't want to send all the ride ro front end so we include Page request.
     * Pageable (Interface), Pagination and sorting
     * PageRequest (impl od Pageable interface)
     *
     * @return
     */
    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        //get current Driver
        Driver currentDriver = getCurrentDriver();
        Page<Ride> rides= rideService.getAllRidesOfDriver(currentDriver,pageRequest);
//        .map(
//                ride -> modelMapper.map(ride, RideDto.class)
//        );
        return (Page<RideDto>) mapper.map(rides,RideDto.class);

    }

    @Override
    public Driver getCurrentDriver() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepository.findByUser(user).orElseThrow( //hardcoded Driver with Id 2
                ()->new ResourceNotFoundException("Driver not associated with user with " +
                        "id "+user.getId()));

    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
       return driverRepository.save(driver);
    }
}
