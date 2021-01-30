package com.parkingfinder.routeservice.service;
import com.parkingfinder.routeservice.constants.Constants;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.parkingfinder.routeservice.model.Point;
import com.parkingfinder.routeservice.model.Route;
import com.parkingfinder.routeservice.model.StreetName;
import com.parkingfinder.routeservice.model.exception.RouteNotFoundException;
import com.parkingfinder.routeservice.model.exception.UserNotFoundException;
import com.parkingfinder.routeservice.repository.PointRepository;
import com.parkingfinder.routeservice.repository.RouteRepository;
import com.parkingfinder.routeservice.repository.StreetNameRepository;
import org.springframework.web.client.RestTemplate;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Route service for fetching routes from start point to end point.
 * Stores in memory routes.
 * @author Veronika Ognjanovska
 **/
@Service
public class RouteService extends BaseService<Route>{
    @Autowired
    protected RestTemplate restTemplateNoLoadBalanced;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private StreetNameRepository streetNameRepository;

    /**
     * Method that parses JSON data to route
     * @param objectJson - object that represents JSON data
     * @return Route
     **/
    @Override
    protected Route parseJson(Object objectJson) {
        Route route = new Route();
        try {
            JSONObject json=(JSONObject) objectJson;
            fillStreetNames( json, route);
            JSONArray coordinates = this.getArrayObject(json);
            for (int i = 0; i < coordinates.size(); i++) {
                Point point=this.getPoint( (JSONArray) coordinates.get(i));
                route.getPoints().add(point);
            }
            route.setTimestamp(ZonedDateTime.now());
        }catch(Exception e){
            throw new RouteNotFoundException();
        }
        return route;
    }

    /**
     * Method that saves geoObject to database.
     * @param route - geoObject(route)
     **/
    @Override
    @Async
    protected void saveToDatabase(Route route) {
        try {
            ResponseEntity<String> result =
                    restTemplate.exchange(Constants.WEB_APP_AUTHENTICATION,
                            HttpMethod.GET, new HttpEntity<>(String.class), String.class);
            if (result.getBody() != null) {
                route.setUserEmail(result.getBody());
                routeRepository.save(route);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method that gets data from external service for specific geoObject.
     * @param s - URI string
     * @return Object - JSON data from external service.
     * @throws ParseException - Signals that an error has been reached unexpectedly while parsing.
     **/
    @Override
    protected Object restTemplateExchange(String s) throws ParseException {
        JSONObject json=null;
        final String response = this.restTemplateNoLoadBalanced.getForObject(s, String.class);
        JSONParser parser = new JSONParser();
        json = (JSONObject) parser.parse(response);
        return json;
    }

    private void fillStreetNames(JSONObject json,Route route){
        try {
            JSONArray j1 = (JSONArray) json.get(Constants.WAYPOINTS);
            if (j1 != null) {
                for (int i = 0; i < j1.size(); i++) {
                    StreetName streetName=this.getStreetName((JSONObject) j1.get(i));
                    route.getStreetNames().add(streetName);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that returns a filtered result of routes based on user previous searches
     * @param email - string that represents user email
     * @return List<Route> - a list of last 5 searched routes by the user
     * @throws UserNotFoundException - when user email has not been found
     */
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

    private Point getPoint(JSONArray coord)
    {
        Double lng = (Double) coord.get(1);
        Double lat = (Double) coord.get(0);
        Point point = new Point();
        point.setLng(lng);
        point.setLat(lat);
        return point;
    }

    private StreetName getStreetName(JSONObject waitpoint)
    {
        String name = (String) waitpoint.get(Constants.NAME);
        StreetName streetName = new StreetName();
        streetName.setStreetName(name);
        return  streetName;
    }

    private JSONArray getArrayObject(JSONObject json)
    {
        JSONArray j2 = (JSONArray) json.get(Constants.ROUTES);
        JSONObject routes = (JSONObject) j2.get(0);
        JSONObject geometry = (JSONObject) routes.get(Constants.GEOMETRY);
        JSONArray coordinates = (JSONArray) geometry.get(Constants.COORDINATES);
        return coordinates;
    }
}
