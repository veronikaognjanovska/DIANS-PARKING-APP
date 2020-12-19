package parkingfinder.rest;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.*;
import parkingfinder.model.Route;
import parkingfinder.service.RouteService;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("/route")
public class RouteFinderController {


    public final RouteService routeService;
    private final Bucket bucket;

    public RouteFinderController(RouteService routeService) {
        this.routeService = routeService;
        Bandwidth limit = Bandwidth.classic(200, Refill.greedy(200, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    //http://localhost:8080/route/drive?lng1=21.455612182617188&lan1=41.986291053745575&lng2=21.440162658691406&lan2=41.98246303636425
    @GetMapping("/drive")
    public Route getRouteDrive(@RequestParam Double lng1, @RequestParam Double lan1,
                                      @RequestParam Double lng2, @RequestParam Double lan2) {
        if (bucket.tryConsume(1)) {
            return this.routeService.findRoute("driving", lng1, lan1, lng2, lan2);
        }

        return new Route();
    }

    //http://localhost:8080/route/walk?lng1=21.455612182617188&lan1=41.986291053745575&lng2=21.440162658691406&lan2=41.98246303636425
    @GetMapping("/walk")
    public Route getRouteWalk(@RequestParam Double lng1, @RequestParam Double lan1,
                          @RequestParam Double lng2, @RequestParam Double lan2) {
        if (bucket.tryConsume(1)) {
            return this.routeService.findRoute("walking", lng1, lan1, lng2, lan2);
        }
        return new Route();
    }

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
