package com.parkingfinder.webapp.dtos;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Simple data transfer object for Route
 * */
@Data
public class RouteDto {

    private List<PointDto> points;

    private Set<StreetNameDto> streetNames;

    private ZonedDateTime timestamp;

    public RouteDto() {
        this.points=new LinkedList<>();
        this.streetNames=new LinkedHashSet<>();
    }
}
