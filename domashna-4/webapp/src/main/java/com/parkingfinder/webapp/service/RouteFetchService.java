package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RouteFetchService {

    @Autowired
    private RestTemplate restTemplate;

    public List<RouteDto> findHistoryRoutes(String email) {
        return Arrays.asList(Objects.requireNonNull(
                restTemplate.exchange("ROUTE-SERVICE/route/history",
                HttpMethod.POST, new HttpEntity<>(email),
                        RouteDto[].class).getBody()));
    }
}
