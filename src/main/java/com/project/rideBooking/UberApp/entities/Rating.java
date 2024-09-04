package com.project.rideBooking.UberApp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_rating_rider", columnList = "rider_id"),
        @Index(name = "idx_rating_driver", columnList = "driver_id")
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    private Ride ride;

    @ManyToOne
    private Rider rider;  //get all the rating for rider

    @ManyToOne
    private Driver driver; //get all the rating for driver

    private Integer driverRating;  //rating for drivers

    private  Integer riderRating; //rating for riders

}
