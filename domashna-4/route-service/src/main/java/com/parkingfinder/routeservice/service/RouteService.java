package com.parkingfinder.routeservice.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.parkingfinder.routeservice.model.Point;
import com.parkingfinder.routeservice.model.Route;
import com.parkingfinder.routeservice.model.StreetName;
import com.parkingfinder.routeservice.model.User;
import com.parkingfinder.routeservice.model.exception.RouteNotFoundException;
import com.parkingfinder.routeservice.model.exception.UserNotFoundException;
import com.parkingfinder.routeservice.repository.PointRepository;
import com.parkingfinder.routeservice.repository.RouteRepository;
import com.parkingfinder.routeservice.repository.StreetNameRepository;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService extends BaseService<Route>{

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private StreetNameRepository streetNameRepository;

    public Route findRoute (String type, Double lng1, Double lan1, Double lng2, Double lan2){
        String url = "https://routing.openstreetmap.de/routed-bike/route/v1/"+type+"/" +
                lng1+","+lan1+";"+lng2+","+lan2+"?overview=full&geometries=geojson";
        return this.findGeoObject(url);
    }


    @Override
    protected Route parseJson(Object objectJson) {
        Route route = new Route();
        try {
            JSONObject json=(JSONObject) objectJson;

            fillStreetNames( json, route);

            JSONArray j2 = (JSONArray) json.get("routes");
            JSONObject routes = (JSONObject) j2.get(0);
            JSONObject geometry = (JSONObject) routes.get("geometry");
            JSONArray coordinates = (JSONArray) geometry.get("coordinates");
            for (int i = 0; i < coordinates.size(); i++) {
                JSONArray coord = (JSONArray) coordinates.get(i);
                Double lng = (Double) coord.get(1);
                Double lat = (Double) coord.get(0);
                Point point = new Point();
                point.setLng(lng);
                point.setLat(lat);
                pointRepository.save(point);
                route.getPoints().add(point);
            }

            route.setTimestamp(ZonedDateTime.now());

        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RouteNotFoundException();
        }

        return route;
    }

    @Override
    @Async
    protected void saveToDatabase(Route route) {
        ResponseEntity<String> result =
                restTemplate.exchange("WEB-APP/authentication",
                        HttpMethod.GET, new HttpEntity<>(String.class), String.class);
        route.setUserEmail(result.getBody());
        routeRepository.save(route);
    }

    @Override
    protected Object restTemplateExchange(String s) throws ParseException {
        JSONObject json=null;
        final String response = this.restTemplate.getForObject(s, String.class);
        JSONParser parser = new JSONParser();
        json = (JSONObject) parser.parse(response);
        return json;
    }


    private void fillStreetNames(JSONObject json,Route route){
        try {
            JSONArray j1 = (JSONArray) json.get("waypoints");
            if (j1 != null) {
                for (int i = 0; i < j1.size(); i++) {
                    JSONObject waitpoint = (JSONObject) j1.get(i);
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


    public List<Route> findHistoryRoutes(String email) throws UserNotFoundException{
        if(email==null){
            throw new UserNotFoundException();
        }
        List<Route> routeList = routeRepository.findAllByUserEmail(email);
        if(routeList==null){
            routeList=new LinkedList<>();
        }
        return routeList;
    }


}
