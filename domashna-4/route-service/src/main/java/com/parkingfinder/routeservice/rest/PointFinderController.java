package com.parkingfinder.routeservice.rest;

import com.parkingfinder.routeservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.parkingfinder.routeservice.model.Point;
import com.parkingfinder.routeservice.service.PointService;

/**
 * Rest controller for accessing location point data
 * Extends BaseController
 * @author Veronika Stefanovska
 */
@RestController
@RequestMapping("/point")
public class PointFinderController extends BaseController{

    @Autowired
    public PointService pointService;

    /**
     * Default controller that sets bucket bandwidth limit
     */
    public PointFinderController()
    {
        this.setBandwidthLimit();
    }

    /**
     * Method that returns location point based on location name
     * @param q - string that represents location name
     * @return Point - location point matching location name or empty point if it does not match
     */
    @GetMapping
    public Point getPointFromName(@RequestParam String q)
    {
        if(bucket.tryConsume(1)) {
            String search = q.replace(Constants.SEARCH_STRING_REPLACE_TARGET,Constants.SEARCH_STRING_REPLACE_REPLACEMENT);
            String url = Constants.POINT_URL_START_PART +search+Constants.POINT_URL_END_PART;
            return pointService.findGeoObject(url);
        }
        return new Point();
    }
}

