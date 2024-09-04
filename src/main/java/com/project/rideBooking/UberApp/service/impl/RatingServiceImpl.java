package com.project.rideBooking.UberApp.service.impl;

import com.project.rideBooking.UberApp.dto.DriverDto;
import com.project.rideBooking.UberApp.dto.RiderDto;
import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Rating;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.Rider;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.exceptions.RuntimeConflictException;
import com.project.rideBooking.UberApp.repository.DriverRepository;
import com.project.rideBooking.UberApp.repository.RatingRepository;
import com.project.rideBooking.UberApp.repository.RiderRepository;
import com.project.rideBooking.UberApp.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper mapper;


    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        //get the Driver associated with the ride
        Driver driver=ride.getDriver();

        //Create Rating object for the Ride (Check if there is Rating for the Ride)
        Rating ratingObj=ratingRepository.findByRide(ride).orElseThrow(
                ()->new ResourceNotFoundException("Rating not Found for the Ride: "+ride.getId()));

        //Check If Driver has already been rated
        if (ratingObj.getDriverRating() != null){
            throw  new RuntimeConflictException("Driver has already been rated, Cannot rate the Driver again");
        }

        //Update the Driver rating with rating provided and save it
        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);

        // Calculate the average rating of the Driver

            //1.get all the ratings of the Driver
           Double avgRating= ratingRepository.findByDriver(driver)         //List<rating>
                                .stream()
                                //2.Convert all the Ratings to Double, use the .average()  of stream
                                .mapToDouble(rating1->rating1.getDriverRating())
                                .average()
                                .orElse(0.0); //default value

        //update the Average rating in Driver Entity
        driver.setRating(avgRating);
        Driver savedDriver = driverRepository.save(driver);

        return mapper.map(savedDriver, DriverDto.class);

    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {

        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: "+ride.getId()));
        if(ratingObj.getRiderRating() != null)
            throw new RuntimeConflictException("Rider has already been rated, cannot rate again");

        ratingObj.setRiderRating(rating);

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getRiderRating)
                .average().orElse(0.0);
        rider.setRating(newRating);
        Rider savedRider = riderRepository.save(rider);
        return mapper.map(savedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) { //New Rating Entity

        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);

    }
}
