package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.ParkingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ParkingSpotsFetchService {
    @Autowired
    private RestTemplate restTemplate;

    public List<ParkingDto> getParkingSpotsAll() {
        return Arrays.asList(Objects.requireNonNull(
                restTemplate.exchange("PARKING-SERVICE/parking/filter/all",
                HttpMethod.GET, null, ParkingDto[].class).getBody()));
    }

    public ParkingDto findById(String id) {
        return restTemplate.exchange("PARKING-SERVICE/parking/filter/single?id=" + id,
                HttpMethod.GET, null, ParkingDto.class).getBody();
    }
}
