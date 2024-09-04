package com.project.rideBooking.UberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {
    private  Long id;

    private UserDto user;

    private Boolean available;   //Rider availability

    private String vehicleId;

    private Double rating;
}
