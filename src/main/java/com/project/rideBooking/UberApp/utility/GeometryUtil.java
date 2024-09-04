package com.project.rideBooking.UberApp.utility;

import com.project.rideBooking.UberApp.dto.PointDTo;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    /**
     * Geometry Factory method(Tell the points are earth coordinates)
     * Coordinate Class-to assign the coordinates from PointDto( coordinates[Longitude,latitude])
     * @param pointDTo (Non static )
     * @return Point
     */
    public static Point createPoint(PointDTo pointDTo) {

        //Geometry Factory method-Create instance,Constructor call
        GeometryFactory geometryFactory=new GeometryFactory(new PrecisionModel(),4326);

        //Coordinate Class-to assign the coordinates from PointDto( coordinates[Longitude,latitude])
        Coordinate coordinate = new Coordinate(pointDTo.getCoordinates()[0],
                pointDTo.getCoordinates()[1]);

        //Convert coordinates and create Point
       return geometryFactory.createPoint(coordinate);

    }
}
