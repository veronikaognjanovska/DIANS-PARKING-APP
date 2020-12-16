package parkingfinder.rest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkingfinder.model.Point;
import parkingfinder.model.Route;
import parkingfinder.model.StreetName;
import parkingfinder.model.exception.UserNotFoundException;
import parkingfinder.service.RouteService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("/route")
public class RouteFinderController {


    public final RouteService routeService;

    public RouteFinderController(RouteService routeService) {
        this.routeService = routeService;
    }

    //http://localhost:8080/route/drive?lng1=21.455612182617188&lan1=41.986291053745575&lng2=21.440162658691406&lan2=41.98246303636425
    @GetMapping("/drive")
    public Route getRouteDrive(@RequestParam Double lng1, @RequestParam Double lan1,
                          @RequestParam Double lng2, @RequestParam Double lan2) {

        return this.routeService.findRoute("driving",lng1, lan1, lng2, lan2);
    }

    //http://localhost:8080/route/walk?lng1=21.455612182617188&lan1=41.986291053745575&lng2=21.440162658691406&lan2=41.98246303636425
    @GetMapping("/walk")
    public Route getRouteWalk(@RequestParam Double lng1, @RequestParam Double lan1,
                          @RequestParam Double lng2, @RequestParam Double lan2) {

        return this.routeService.findRoute("walking",lng1, lan1, lng2, lan2);
    }

    @GetMapping("/history")
    public List<Route> getRouteHistory(HttpServletRequest req) {
        try {
            return this.routeService.findHistoryRoutes(req.getRemoteUser());
        }catch (UserNotFoundException ex){
            return new LinkedList<>();
        }

    }






}
