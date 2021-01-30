package com.parkingfinder.webapp.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Simple value store for application properties base urls
 * */
@Component
@PropertySource("classpath:bootstrap.yml")
@Data
public class LocalBaseUrls {
    @Value("${base.url.parking.service}")
    private String parkingService;
    @Value("${base.url.point.service}")
    private String pointService;
    @Value("${base.url.route.service}")
    private String routeService;
}
