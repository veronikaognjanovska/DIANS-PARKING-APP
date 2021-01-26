package com.parkingfinder.webapp.dtos;

import lombok.Data;

/**
 * Simple data transfer object for Point
 * */
@Data
public class PointDto {
    private Double lat;
    private Double lng;
}
