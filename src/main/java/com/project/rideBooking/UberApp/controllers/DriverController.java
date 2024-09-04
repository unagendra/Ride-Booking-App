package com.project.rideBooking.UberApp.controllers;

import com.project.rideBooking.UberApp.dto.*;
import com.project.rideBooking.UberApp.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver")
@Secured("ROLE_DRIVER")
public class DriverController {

    private final DriverService driverService;

    /**
     * Driver has to accept the RideRequest
     * After rideRequest is accepted, Ride is created
     * Here you are returning the Ride information to the Frontend
     *
     * @param rideRequestId
     * @return RideDto
     */
    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId) {

        return new ResponseEntity<>(driverService.acceptRide(rideRequestId), HttpStatus.OK);
    }

    /**
     * Driver startRide ones he verifies the otp sent to the Rider
     * RideStartDto- Contains the otp
     *
     * @param rideRequestId
     * @param rideStartDto
     * @return RideDto
     */
    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideRequestId,
                                             @RequestBody RideStartDto rideStartDto) {
        return ResponseEntity.ok(driverService.startRide(rideRequestId, rideStartDto.getOtp()));
    }

    /**
     * Driver end the ride,once ride is completed.
     * Payment call is made here
     *
     * @param rideId
     * @return
     */
    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDto> endRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        return new ResponseEntity<>(driverService.cancelRide(rideId), HttpStatus.OK);
    }

    /**
     * Driver Rating - Rider rates the driver for every ride(rideId)
     *
     * @param ratingDto
     * @return
     */
    @PostMapping("/rateDriver")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto) {

        return new ResponseEntity<>(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()), HttpStatus.OK);
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDto> getMyProfile() {
        return new ResponseEntity<>(driverService.getMyProfile(), HttpStatus.OK);
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        //create PageRequest object(pageOffset+pageSize+Sorting)
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "id"));

        return new ResponseEntity<>(driverService.getAllMyRides(pageRequest), HttpStatus.OK);

    }


}


