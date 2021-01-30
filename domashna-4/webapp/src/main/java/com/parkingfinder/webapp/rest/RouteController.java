package com.parkingfinder.webapp.rest;

import com.parkingfinder.webapp.dtos.PointDto;
import com.parkingfinder.webapp.dtos.RouteDto;
import com.parkingfinder.webapp.service.RouteFetchService;
import com.parkingfinder.webapp.util.URLPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *Rest controller for fetching geo objects
 * */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteFetchService routeFetchService;

    /**
     * Returns a resolved route for pedestrians based on starting and ending point geo coordinates
     * returns null if an exception occurred
     * @return RouteDto - resolved route or null
     * */
    @GetMapping("/walk")
    public RouteDto getRouteWalk(@RequestParam Double lng1, @RequestParam Double lan1,
                             @RequestParam Double lng2, @RequestParam Double lan2) {
        try {
            return routeFetchService.findRoute(lng1, lan1, lng2, lan2, URLPaths.ROUTE_SERVICE_WALK);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a resolved route for vehicles based on starting and ending point geo coordinates
     * returns null if an exception occurred
     * @return RouteDto - resolved route or null
     * */
    @GetMapping("/drive")
    public RouteDto getRouteDrive(@RequestParam Double lng1, @RequestParam Double lan1,
                                 @RequestParam Double lng2, @RequestParam Double lan2) {
        try {
            return routeFetchService.findRoute(lng1, lan1, lng2, lan2, URLPaths.ROUTE_SERVICE_DRIVE);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a resolved geo location with coordinates based on an input address
     * returns null if an exception occurred
     * @return PointDto - resolved point or null
     * */
    @GetMapping("/point")
    public PointDto getPoint(@RequestParam String q) {
        try {
            return routeFetchService.findPoint(q);
        }catch (Exception e) {
            return null;
        }
    }
}
