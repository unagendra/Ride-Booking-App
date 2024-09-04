package com.project.rideBooking.UberApp.repository;


import com.project.rideBooking.UberApp.dto.UserDto;
import com.project.rideBooking.UberApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //checks if the email is present in User Entity and returns User
   Optional<User>  findByEmail(String email);
}
