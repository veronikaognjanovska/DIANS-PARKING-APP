package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.ParkingDto;
import com.parkingfinder.webapp.util.URLPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service for communicating with parking microservice
 * */
@Service
public class ParkingSpotsFetchService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetches all the parking spots from the external parking microservice
     * @return List - list of parking spot data transfer objects
     * */
    public List<ParkingDto> getParkingSpotsAll() {
        return getParkingList(URLPaths.PARKING_SERVICE_BASE_URL + URLPaths.PARKING_FILTER_ALL);
    }

    /**
     * Fetches a single parking spot from the external parking microservice
     * @param id - string that represents the requested id
     * @return ParkingDto - parking spot data transfer object matching requested id or null if not present
     * */
    public ParkingDto findById(String id) {
        return restTemplate.exchange(URLPaths.PARKING_SERVICE_BASE_URL
                        + URLPaths.PARKING_FILTER_SINGLE + id,
                HttpMethod.GET, null, ParkingDto.class).getBody();
    }

    /**
     * Fetches all the parking spots from the external parking microservice
     * that match an access level
     * @param access - String representing the requested access level
     * @return List - list of parking spot data transfer objects
     * */
    public List<ParkingDto> getParkingByAccessLevel(String access) {
        return getParkingList(URLPaths.PARKING_SERVICE_BASE_URL
                + URLPaths.PARKING_FILTER_BY_ACCESS + access);
    }

    /**
     * Fetches all the parking spots from the external parking microservice
     * that match a parking type
     * @param type - String representing the requested parking type
     * @return List - list of parking spot data transfer objects
     * */
    public List<ParkingDto> getParkingByType(String type) {
        return getParkingList(URLPaths.PARKING_SERVICE_BASE_URL
                + URLPaths.PARKING_FILTER_BY_TYPE + type);
    }

    private List<ParkingDto> getParkingList(String url) {
        return Arrays.asList(Objects.requireNonNull(
                restTemplate.exchange(url, HttpMethod.GET,
                        null, ParkingDto[].class).getBody()));
    }
}
