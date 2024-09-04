package com.project.rideBooking.UberApp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_driver_vehicle_id", columnList = "vehicleId")
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private  Long id;

    @OneToOne
    @JoinColumn(name = "user_id")    //Foreign key column for User(id), with One to One
    private User user;

    private Double rating; //average rating of the Driver

    private Boolean available;   //Driver availability

    private String vehicleId;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    Point currentLocation;



}
