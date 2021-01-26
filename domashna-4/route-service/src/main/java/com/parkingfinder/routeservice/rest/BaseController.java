package com.parkingfinder.routeservice.rest;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

public abstract class BaseController {

    protected  Bucket bucket;
    
    public Bandwidth setBandwidthLimit(){
        Bandwidth limit = Bandwidth.classic(200, Refill.greedy(200, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        return limit;
    }
    
    
    
}


