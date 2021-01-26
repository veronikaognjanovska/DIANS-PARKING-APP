package com.parkingfinder.webapp.dtos;

import lombok.Data;

@Data
public class ParkingDto {
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
