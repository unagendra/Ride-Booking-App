package com.project.rideBooking.UberApp.service.impl;


import com.project.rideBooking.UberApp.dto.DriverDto;
import com.project.rideBooking.UberApp.dto.RideDto;
import com.project.rideBooking.UberApp.dto.RideRequestDTO;
import com.project.rideBooking.UberApp.dto.RiderDto;
import com.project.rideBooking.UberApp.entities.*;
import com.project.rideBooking.UberApp.entities.enums.RideRequestStatus;
import com.project.rideBooking.UberApp.entities.enums.RideStatus;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.repository.RideRequestRepository;
import com.project.rideBooking.UberApp.repository.RiderRepository;
import com.project.rideBooking.UberApp.service.DriverService;
import com.project.rideBooking.UberApp.service.RatingService;
import com.project.rideBooking.UberApp.service.RideService;
import com.project.rideBooking.UberApp.service.RiderService;
import com.project.rideBooking.UberApp.strategies.DriverMatchingStrategy;
import com.project.rideBooking.UberApp.strategies.RideFareCalculationStrategy;
import com.project.rideBooking.UberApp.strategies.RideStrategyManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

   private final ModelMapper mapper;
   private final RiderRepository riderRepository;
   private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private  final RideService rideService;
    private  final DriverService driverService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDto) {
        //get the current rider information who is requesting a ride
        Rider rider = getCurrentRider();

        //convert rideRequestDTO to rideRequest
        RideRequest rideRequest=mapper.map(rideRequestDto, RideRequest.class);
       // log.info(rideRequest.toString());

        //update the rideRequestStatus as PENDING
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        //calculate the Fare passing rideRequestDto
       double fare= rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
       //update the fare in rideRequest entity.
       rideRequest.setFare(fare);

       //save the rideRequest in the RideRequestRepository(Database) after calculating the fare (This is must whether you found the driver or not)
      RideRequest savedRequest= rideRequestRepository.save(rideRequest);

       //Find the Matching Driver(Broadcast to all the drivers we cant do anything here drivers must accept the rideRequest)
        List<Driver> drivers=rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

//        TODO : Send notification to all the drivers about this ride request
        //Driver acceptRide(savedRequest)->Ride


        //convert the savedRequest to rideRequestDTO and return to Controller
        return mapper.map(savedRequest,RideRequestDTO.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        //get current rider
        Rider rider = getCurrentRider();
        //get the ride from rideId
        Ride ride = rideService.getRideById(rideId);

        //check if rider owns the ride
        if(!rider.equals(ride.getRider())) {
            throw new RuntimeException(("Rider does not own this ride with id: "+rideId));
        }

        //Cancel logic-> Rider Cancel ride only if the rideStatus is CONFIRMED
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        //Set the Ride status to cancelled
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.CANCELLED);

        // Make theDriver available,So he can accept other ride
        driverService.updateDriverAvailability(ride.getDriver(),true);


        return mapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if(!rider.equals(ride.getRider())) {
            throw new RuntimeException("Rider is not the owner of this Ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: "+ride.getRideStatus());
        }

        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider = getCurrentRider();
        return mapper.map(currentRider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider, pageRequest).map(
                ride -> mapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {

      //Rider rider=new  Rider();
        Rider rider=Rider.builder()
                .user(user)
                .rating(0.0)
                .build();

        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        //TODO SPRING SECURITY

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return riderRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException(
                "Rider not associated with user with id: "+user.getId()
        ));
    }

}




