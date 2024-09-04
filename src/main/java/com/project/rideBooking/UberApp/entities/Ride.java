package com.project.rideBooking.UberApp.entities;

import com.project.rideBooking.UberApp.entities.enums.PaymentMethod;
import com.project.rideBooking.UberApp.entities.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_ride_rider", columnList = "rider_id"),
        @Index(name = "idx_ride_driver", columnList = "driver_id")
})
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private  Long id;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime createdTime;

   @ManyToOne(fetch = FetchType.LAZY)
    private  Rider rider;

   @ManyToOne(fetch =FetchType.LAZY)
    private  Driver driver;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    private  Double fare;

    private  String otp;

    private LocalDateTime startedAt;


    private LocalDateTime endedAt;




}
