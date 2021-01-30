package com.parkingfinder.routeservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.parkingfinder.routeservice.model.Route;
import com.parkingfinder.routeservice.repository.PointRepository;
import com.parkingfinder.routeservice.repository.RouteRepository;
import com.parkingfinder.routeservice.repository.StreetNameRepository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service for evicting routes older than 7 days
 */
@Component
@EnableAsync
public class RouteEvictionService {

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private StreetNameRepository streetNameRepository;

    /**
     * Asynchronous method for evicting routes older than 7 days
     * Scheduled every night at 1 am
     * @return Integer - number od routes deleted
     */
    @Async
    @Scheduled(cron = "0 1 1 * * ?")
    public Integer evictOldRoutes() {
        List<Route> routes = routeRepository.findAll();
        List<Route> toDelete = routes.stream()
                .filter(route -> route.getTimestamp()!=null)
                .filter(route -> Math.abs(Duration.between(ZonedDateTime.now(),
                        route.getTimestamp()).toDays()) > 7).collect(Collectors.toList());
        int deleted = toDelete.size();
        routeRepository.deleteAll(toDelete);
        return deleted;
    }
}
