package com.parkingfinder.routeservice.constants;
import org.springframework.stereotype.Component;

/**
 * Utility class that holds values for constants used in microservice
 * */
@Component
public class Constants {
    public static final String POINT_URL_START_PART = "https://nominatim.openstreetmap.org/search?q=";
    public static final String POINT_URL_END_PART= "&limit=1&format=json";
    public static final String SEARCH_STRING_REPLACE_TARGET= " ";
    public static final String SEARCH_STRING_REPLACE_REPLACEMENT= "-";
    public static final String LONGITUDE="lon";
    public static final String LATITUDE="lat";
    public static final String ROUTE_URL_START_PART = "https://routing.openstreetmap.de/routed-bike/route/v1/";
    public static final String ROUTE_URL_END_PART= "?overview=full&geometries=geojson";
    public static final String ROUTES="routes";
    public static final String GEOMETRY="geometry";
    public static final String COORDINATES="coordinates";
    public static final String WAYPOINTS="waypoints";
    public static final String NAME="name";
    public static final String WEB_APP_AUTHENTICATION="http://WEB-APP/authentication";
}

