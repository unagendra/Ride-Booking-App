package com.project.rideBooking.UberApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)   //user  has to be submitted (Mandatory)
    private User user;                                   //Sign up (Creating new wallet)

    private Double balance=0.0;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<WalletTransaction> transactions;
}
