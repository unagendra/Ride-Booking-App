package com.project.rideBooking.UberApp.repository;


import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.Ride;
import com.project.rideBooking.UberApp.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    //custom methods() ->Repo create instance of Pageable not page request (you are passing page request which is of type pageable)
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);
}
