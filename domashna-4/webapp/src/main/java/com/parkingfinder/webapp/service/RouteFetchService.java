package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.PointDto;
import com.parkingfinder.webapp.dtos.RouteDto;
import com.parkingfinder.webapp.util.URLPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service for communicating with external route microservice
 * */
@Service
public class RouteFetchService {

    @Autowired
    private RestTemplate restTemplate;

    /**
    * Method that finds the current logged in user's route history
    * @param email - currently logged in user's email
    * @return List - list of routes searched by the user
    * */
    public List<RouteDto> findHistoryRoutes(String email) {
        return Arrays.asList(Objects.requireNonNull(
                restTemplate.exchange(URLPaths.ROUTE_SERVICE_BASE_URL
                                + URLPaths.ROUTE_SERVICE_HISTORY,
                HttpMethod.POST, new HttpEntity<>(email),
                        RouteDto[].class).getBody()));
    }

    /**
     * Method that finds a route based on start and end latitude and longitude
     * @param lat1 - latitude of starting point
     * @param lng1 - longitude of starting point
     * @param lat2 - latitude of ending point
     * @param lng2 - longitude of ending point
     * @param routeType - type of route to fetch: walk or drive
     * @return RouteDto - route that was resolved
     * */
    public RouteDto findRoute(Double lat1, Double lng1, Double lat2, Double lng2, String routeType) {
        return restTemplate.exchange(URLPaths.ROUTE_SERVICE_BASE_URL
                        + routeType + "?lng1=" +
                        lat1 + "&lan1=" + lng1 + "&lng2="
                        + lat2 + "&lan2=" + lng2,
                HttpMethod.GET, null,
                RouteDto.class).getBody();
    }

    /**
     * Method that finds a point with latitude and longitude on the map based on an input address
     * @param query - query address
     * @return PointDto - resolved point with latitude and longitude
     * */
    public PointDto findPoint(String query) {
        return restTemplate.exchange(URLPaths.POINT_SERVICE_BASE_URL + "?q="+query,
                HttpMethod.GET, null, PointDto.class).getBody();
    }
}
