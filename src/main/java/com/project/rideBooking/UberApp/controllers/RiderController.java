package com.project.rideBooking.UberApp.controllers;

import com.project.rideBooking.UberApp.dto.*;
import com.project.rideBooking.UberApp.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {

   private final RiderService riderService;

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(@RequestBody RideRequestDTO rideRequestDTO){

        return new ResponseEntity<>(riderService.requestRide(rideRequestDTO), HttpStatus.OK);
       // return ResponseEntity.ok(riderService.requestRide(rideRequestDto));

    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId){
        return new ResponseEntity<>(riderService.cancelRide(rideId), HttpStatus.OK);
    }

    /**
     * Driver Rating - Rider rates the driver for every ride(rideId)
     * @param ratingDto
     * @return
     */
    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto){

        return new ResponseEntity<>(riderService.rateDriver(ratingDto.getRideId(), ratingDto.getRating()), HttpStatus.OK);
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDto> getMyProfile(){
        return new ResponseEntity<>(riderService.getMyProfile(), HttpStatus.OK);
    }

@GetMapping("/getMyRides")
    public  ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                        @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        //create PageRequest object(pageOffset+pageSize+Sorting)
    PageRequest pageRequest=PageRequest.of(pageOffset, pageSize,
            Sort.by(Sort.Direction.DESC, "createdTime", "id"));

    return new ResponseEntity<>(riderService.getAllMyRides(pageRequest), HttpStatus.OK);

}

}
