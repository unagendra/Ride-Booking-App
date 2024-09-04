package com.project.rideBooking.UberApp.dto;

import com.project.rideBooking.UberApp.entities.Rider;
import com.project.rideBooking.UberApp.entities.enums.PaymentMethod;
import com.project.rideBooking.UberApp.entities.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {


    private  Long id;

    //we need to provide own implementation for the points(jackson will not serialize it)
    private PointDTo pickupLocation;

    private PointDTo dropOffLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider ;  //Foreign Key Rider(id)

    private Double fare;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;
}
