package com.parkingfinder.parkingservice.util;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Utility class that holds values for constants used in microservice
 * */
@Component
public class Constants {
    public static final String DEFAULT_ACCESS_LEVEL = "public";
    public static final String DEFAULT_PARKING_TYPE = "surface";
}
