package com.parkingfinder.routeservice.rest;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.*;
import com.parkingfinder.routeservice.model.Point;
import com.parkingfinder.routeservice.service.PointService;

import java.time.Duration;

@RestController
@RequestMapping("/point")
public class PointFinderController extends BaseController{
    
    public final PointService pointService;
    //private Bucket bucket;

    public PointFinderController(PointService pointService)
    {
        this.pointService=pointService;

        this.setBandwidthLimit();
    }

    @GetMapping
    public Point getPointFromName(@RequestParam String q)
    {
        if(bucket.tryConsume(1)) {
            return this.pointService.getPointFromName(q);
        }
        return new Point();
    }
}

