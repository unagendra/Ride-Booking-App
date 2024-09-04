package com.project.rideBooking.UberApp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private  Long id;

    @OneToOne
    @JoinColumn(name = "user_id")    //Foreign key column for User(id), with One to One
    private User user;

    private Double rating;


//    public Rider(User user, Double rating, Long id) {
//        this.user = user;
//        this.rating = rating;
//        this.id = id;
//    }
}
