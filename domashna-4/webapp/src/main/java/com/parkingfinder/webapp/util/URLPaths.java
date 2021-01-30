package com.parkingfinder.webapp.util;

/**
 * Utility class for commonly used paths in application
 * */
public class URLPaths {

    //parking microservice
    public static final String PARKING_SERVICE_BASE_URL = "http://PARKING-SERVICE/parking/filter/";
    public static final String PARKING_FILTER_ALL = "all";
    public static final String PARKING_FILTER_BY_ACCESS = "access?accessLevel=";
    public static final String PARKING_FILTER_BY_TYPE = "type?parkingType=";
    public static final String PARKING_FILTER_SINGLE = "single?id=";

    //user microservice
    public static final String USER_SERVICE_BASE_URL = "http://USER-SERVICE/user/";
    public static final String USER_DETAILS = "user-details";
    public static final String USER_REGISTER = "register";
    public static final String USER_UPDATE = "update";

    //route microservice
    public static final String ROUTE_SERVICE_BASE_URL = "http://ROUTE-SERVICE/route/";
    public static final String POINT_SERVICE_BASE_URL = "http://ROUTE-SERVICE/point/";
    public static final String ROUTE_SERVICE_HISTORY = "history";
    public static final String ROUTE_SERVICE_WALK = "walk";
    public static final String ROUTE_SERVICE_DRIVE = "drive";

}
