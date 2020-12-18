package parkingfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import parkingfinder.model.Route;
import parkingfinder.repository.RouteRepository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableAsync
public class RouteEvictionService {

    @Autowired
    private RouteRepository routeRepository;

    @Async
    @Scheduled(cron = "0 1 1 * * ?")
    public int evictOldRoutes() {
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
