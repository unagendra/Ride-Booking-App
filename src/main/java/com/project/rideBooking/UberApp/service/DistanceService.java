package com.project.rideBooking.UberApp.service;

import org.locationtech.jts.geom.Point;

public interface DistanceService {

    double calculateDistance(Point src, Point dest);
}
