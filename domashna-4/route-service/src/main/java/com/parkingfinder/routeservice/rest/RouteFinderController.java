package com.parkingfinder.routeservice.rest;

import com.parkingfinder.routeservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.parkingfinder.routeservice.model.Route;
import com.parkingfinder.routeservice.service.RouteService;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Rest controller for accessing route data
 * Extends BaseController
 * @author Veronika Ognjanovska
 */
@RestController
@RequestMapping("/route")
public class RouteFinderController extends BaseController{
    @Autowired
    public  RouteService routeService;

    /**
     * Default constructor that sets bucket bandwidth limit
     */
    public RouteFinderController() {
        this.setBandwidthLimit();
    }

    /**
     * Method that returns route based on start and end points for option driving
     * @param lng1 - longitude for start point
     * @param lan1 - latitude for start point
     * @param lng2 - longitude for end point
     * @param lan2 - latitude for end point
     * @return Route - route with included points and street names
     */
    @GetMapping("/drive")
    public Route getRouteDrive(@RequestParam Double lng1, @RequestParam Double lan1,
                               @RequestParam Double lng2, @RequestParam Double lan2) {
        if (bucket.tryConsume(1)) {
            String url = Constants.ROUTE_URL_START_PART +"driving/" +
                    lng1+","+lan1+";"+lng2+","+lan2+Constants.ROUTE_URL_END_PART;
            return routeService.findGeoObject(url);
        }
        return new Route();
    }

    /**
     * Method that returns route based on start and end points for option walking
     * @param lng1 - longitude for start point
     * @param lan1 - latitude for start point
     * @param lng2 - longitude for end point
     * @param lan2 - latitude for end point
     * @return Route - route with included points and street names
     */
    @GetMapping("/walk")
    public Route getRouteWalk(@RequestParam Double lng1, @RequestParam Double lan1,
                              @RequestParam Double lng2, @RequestParam Double lan2) {
        if (bucket.tryConsume(1)) {
            String url = Constants.ROUTE_URL_START_PART +"walking/" +
                    lng1+","+lan1+";"+lng2+","+lan2+Constants.ROUTE_URL_END_PART;
            return routeService.findGeoObject(url);
        }
        return new Route();
    }

    /**
     * Method that returns a filtered result of routes based on user previous searches
     * @param req - http servlet request
     * @return List<Route> - a list of last 5 searched routes by the user
     */
    @GetMapping("/history")
    public List<Route> getRouteHistory(HttpServletRequest req) {
        try {
            bucket.tryConsume(1);
            return this.routeService.findHistoryRoutes(req.getRemoteUser());
        }catch (Exception ex){
            return new LinkedList<>();
        }

    }

}
