package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.RouteDto;
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
                restTemplate.exchange("http://ROUTE-SERVICE/route/history",
                HttpMethod.POST, new HttpEntity<>(email),
                        RouteDto[].class).getBody()));
    }
}
