package parkingfinder.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkingfinder.model.Point;
import parkingfinder.model.Route;
import parkingfinder.model.StreetName;
import parkingfinder.model.User;
import parkingfinder.model.exception.RouteNotFoundException;
import parkingfinder.model.exception.UserNotFoundException;
import parkingfinder.repository.PointRepository;
import parkingfinder.repository.RouteRepository;
import parkingfinder.repository.StreetNameRepository;
import parkingfinder.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final PointRepository pointRepository;
    private final StreetNameRepository streetNameRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate ;


    public RouteService(RouteRepository routeRepository, PointRepository pointRepository, StreetNameRepository streetNameRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.routeRepository = routeRepository;
        this.pointRepository = pointRepository;
        this.streetNameRepository = streetNameRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public List<Route> findHistoryRoutes(String email) throws UserNotFoundException{
        if(email==null){
            throw new UserNotFoundException();
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }
        List<Route> routeList = routeRepository.findAllByUserId(userOpt.get());
        if(routeList==null){
            routeList=new LinkedList<>();
        }
        return routeList;
    }


    public Route findRoute (String type, Double lng1, Double lan1, Double lng2, Double lan2){
        String url = "https://routing.openstreetmap.de/routed-bike/route/v1/"+type+"/" +
                lng1+","+lan1+";"+lng2+","+lan2+"?overview=full&geometries=geojson";

        Route route = new Route();
        try {
            JSONObject json=sentRequestObject(url);

            fillStreetNames( json, route);

            JSONArray j2 = (JSONArray) json.get("routes");
            JSONObject routes = (JSONObject) j2.get(0);
            JSONObject geometry = (JSONObject) routes.get("geometry");
            JSONArray coordinates = (JSONArray) geometry.get("coordinates");
            for (int i = 0; i < coordinates.size(); i++) {
                JSONArray coord = (JSONArray) coordinates.get(i);
                // location
                Double lng = (Double) coord.get(1);
                Double lat = (Double) coord.get(0);
                Point point = new Point();
                point.setLng(lng);
                point.setLat(lat);
                pointRepository.save(point);
                route.getPoints().add(point);
            }
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()){
                route.setUserId(user.get());
                route.setTimestamp(ZonedDateTime.now());
                routeRepository.save(route);
            }

        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RouteNotFoundException();
        }

        return route;
    }


    private void fillStreetNames(JSONObject json,Route route){
        try {
            JSONArray j1 = (JSONArray) json.get("waypoints");
            if (j1 != null) {
                for (int i = 0; i < j1.size(); i++) {
                    JSONObject waitpoint = (JSONObject) j1.get(i);
                    // name waitpoint
                    String name = (String) waitpoint.get("name");
                    StreetName streetName = new StreetName();
                    streetName.setStreetName(name);
                    streetNameRepository.save(streetName);
                    route.getStreetNames().add(streetName);
                }
            }
        }catch(Exception e){
            //..
        }
    }


    private  JSONObject sentRequestObject(String url){

        //adding the query params to the URL
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        JSONObject json=null;
        try {
            final String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(response);
        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RuntimeException(e);
        }
        return json;
    }



}
