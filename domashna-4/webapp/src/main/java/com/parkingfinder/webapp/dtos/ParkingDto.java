package com.parkingfinder.webapp.dtos;

import lombok.Data;

/**
 * Simple data transfer object for parking spots
 * */
@Data
public class ParkingDto {
    private String id;
    private double lat;
    private double lng;
    private String name;
    private Integer capacity;
    private String access;
    private String fee;
    private String operator;
    private String website;
    private String supervised;
    private String parking_type;
}
