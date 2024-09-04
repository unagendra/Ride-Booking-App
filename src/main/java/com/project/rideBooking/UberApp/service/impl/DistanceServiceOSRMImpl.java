package com.project.rideBooking.UberApp.service.impl;


import com.project.rideBooking.UberApp.service.DistanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final String OSRM_API_BASE_URL = "https://router.project-osrm.org/route/v1/driving/";

//    private static final String OSRM_API_BASE_URL="https://router.project-osrm.org/route/v1/driving/13.388860,52.517037;13.397634,52.529407";

    @Override
    public double calculateDistance(Point src, Point dest) {

        try {
            String uri = src.getX() + "," + src.getY() + ";" + dest.getX() + "," + dest.getY();

            //TODO Call the third party api called OSRM to fetch the distance

            OSRMResponseDto responseDTO = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);

            //distance in meters is returned so convert it into km (/1000-> km)
            return responseDTO.getRoutes().get(0).getDistance() / 1000.0;

        } catch (Exception e) {
            throw new RuntimeException("Error getting data from OSRM " + e.getMessage());
        }


    }
}

    @Data
    class OSRMResponseDto {
        //Custom class to provide custom implementation, Here we are only interested in routes(distance)
        private List<OSRMRoute> routes;
    }

    @Data
    class OSRMRoute {
        private Double distance;
    }

