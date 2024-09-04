package com.project.rideBooking.UberApp.entities;

import com.project.rideBooking.UberApp.entities.enums.PaymentMethod;
import com.project.rideBooking.UberApp.entities.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "idx_ride_request_rider", columnList = "rider_id")
        }
)
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private  Long id;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;

    //used to automatically populate a timestamp field with the current date and time when an entity is first persisted.
    // This is particularly useful for audit purposes, such as keeping track of when records were created
    //Need not specify the Timestamp manually inside Constructor, LocalDateTime.now()
    @CreationTimestamp
    private LocalDateTime requestedTime;

    //GET REQUEST to RideRequest, FetchType.LAZY will not pull the rider information

    //@JoinColumn(name = "rider_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private  Rider rider;  //Foreign Key Rider(id)


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;

    private Double fare;

    @Override
    public String toString() {
        return "RideRequest{" +
                "id=" + id +
                ", pickupLocation=" + pickupLocation +
                ", dropOffLocation=" + dropOffLocation +
                ", requestedTime=" + requestedTime +
                ", rider=" + rider +
                ", paymentMethod=" + paymentMethod +
                ", rideRequestStatus=" + rideRequestStatus +
                '}';
    }
}
