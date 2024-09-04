package com.project.rideBooking.UberApp.entities;

import com.project.rideBooking.UberApp.entities.enums.TransactionMethod;
import com.project.rideBooking.UberApp.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_wallet_transaction_wallet", columnList = "wallet_id"),
        @Index(name = "idx_wallet_transaction_ride", columnList = "ride_id")
})
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private Long id;

   private Double amount;  //amount spent on single transaction

    @Enumerated(EnumType.STRING)
   private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
   private TransactionMethod transactionMethod;

   @ManyToOne
   private  Ride ride;  //Every transaction is  associated with a ride
    //If there is no transaction associated with a ride then it is related to banking

    //Banking related Transaction
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime timeStamp;
}
