package parkingfinder.service;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import parkingfinder.model.Route;
import parkingfinder.repository.PointRepository;
import parkingfinder.repository.RouteRepository;
import parkingfinder.repository.StreetNameRepository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableAsync
public class RouteEvictionService {

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private StreetNameRepository streetNameRepository;

    @Async
    @Scheduled(cron = "0 1 1 * * ?")
    public Integer evictOldRoutes() {
        List<Route> routes = routeRepository.findAll();
        List<Route> toDelete = routes.stream()
                .filter(route -> route.getTimestamp()!=null)
                .filter(route -> Math.abs(Duration.between(ZonedDateTime.now(),
                        route.getTimestamp()).toDays()) > 7).collect(Collectors.toList());
        int deleted = toDelete.size();
        System.out.println("To delete: " +deleted);
        toDelete.stream()
                .map(Route::getPoints)
                .filter(p -> p!=null && !p.isEmpty())
                .forEach(p -> pointRepository.deleteAll(p));
        toDelete.stream()
                .map(Route::getStreetNames)
                .filter(s -> s!=null && !s.isEmpty())
                .forEach(s -> streetNameRepository.deleteAll(s));
        routeRepository.deleteAll(toDelete);
        return deleted;
    }
}
