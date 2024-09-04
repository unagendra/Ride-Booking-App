package com.project.rideBooking.UberApp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PointDTo {
    private  double[] coordinates;

    //hardcoded
    private String type="Point";

    public PointDTo(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
