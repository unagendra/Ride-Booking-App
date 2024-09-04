package com.project.rideBooking.UberApp.configs;

import com.project.rideBooking.UberApp.dto.PointDTo;
import com.project.rideBooking.UberApp.utility.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper=new ModelMapper();

        //configure the Model mapper to convert PointDTO to Point
        mapper.typeMap(PointDTo.class, Point.class).setConverter(Context -> {
            //To provide the source
            PointDTo pointDTo=new PointDTo();
            pointDTo= Context.getSource();

            //call the Create Point utility method in Geometry util class(return Point)
           return GeometryUtil.createPoint(pointDTo);
        });

        //configure the Model mapper to convert Point to PointDTO
        mapper.typeMap(Point.class,PointDTo.class).setConverter(Context -> {
            //To provide the source
            Point point= Context.getSource();

            double[] coordinates = {
                    point.getX(),
                    point.getY()
            };

            //return PointDTO object
            return new PointDTo(coordinates);
        });

        return mapper;
    }
}
