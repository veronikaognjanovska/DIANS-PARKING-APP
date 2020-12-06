package parkingapp.app.rest;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkingapp.app.model.Point;
import parkingapp.app.model.Route;
import parkingapp.app.model.StreetName;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

@RestController

//@Controller
@RequestMapping("/api")
public class RouteController {

    public JSONObject sentRequestObject(String url){
        RestTemplate restTemplate = new RestTemplate();
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

    public JSONArray sentRequestArray(String url){
        RestTemplate restTemplate = new RestTemplate();
        //adding the query params to the URL
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        JSONArray json=null;
        try {

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
            String response = responseEntity.getBody();
            JSONParser parser = new JSONParser();
            json = (JSONArray) parser.parse(response);
        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RuntimeException(e);
        }
        return json;
    }


    //http://localhost:8080/api/route?lng1=21.455612182617188&lan1=41.986291053745575&lng2=21.440162658691406&lan2=41.98246303636425
    @GetMapping("/route")
    public Route getRoute(@RequestParam Double lng1, @RequestParam Double lan1,
                         @RequestParam Double lng2, @RequestParam Double lan2) {

        String url = "https://routing.openstreetmap.de/routed-bike/route/v1/driving/" +
                lng1+","+lan1+";"+lng2+","+lan2+"?overview=full&geometries=geojson";

        JSONObject json=sentRequestObject(url);

        Route route = new Route();
        try {

            JSONArray j1=(JSONArray)json.get("waypoints");
            for (int i = 0; i < j1.size(); i++) {
                JSONObject waitpoint=(JSONObject)j1.get(i);
                // name waitpoint
                String name=(String)waitpoint.get("name");
                StreetName streetName=new StreetName();
                streetName.setStreetName(name);
                route.getStreetNames().add(streetName);
//                // location waitpoint
//                JSONArray location=(JSONArray)waitpoint.get("location");
//                Double lng=(Double) location.get(1);
//                Double lat=(Double)location.get(0);
//                Point point = new Point();
//                point.setLng(lng);
//                point.setLat(lat);
//                route.getPoints().add(point);
            }

            JSONArray j2=(JSONArray)json.get("routes");
            JSONObject routes=(JSONObject)j2.get(0);
            JSONObject geometry=(JSONObject)routes.get("geometry");
            JSONArray coordinates=(JSONArray)geometry.get("coordinates");
            for (int i = 0; i < coordinates.size(); i++) {
                JSONArray coord=(JSONArray)coordinates.get(i);
                // location
                Double lng=(Double) coord.get(1);
                Double lat=(Double) coord.get(0);
                Point point = new Point();
                point.setLng(lng);
                point.setLat(lat);
                route.getPoints().add(point);
            }


        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RuntimeException(e);
        }

        return route;
    }


    // http://localhost:8080/api/name?q=New York City
    @GetMapping("/name")
    public Point getPointFromName(@RequestParam String q) {

         String url = "https://nominatim.openstreetmap.org/search?q="+q+"&limit=1&format=json";
        JSONArray json=sentRequestArray(url);

        Point point = new Point();
        try {
            if(!json.isEmpty()){

            JSONObject j1=(JSONObject)json.get(0);
            Double lng=Double.parseDouble((String) j1.get("lon"));
            Double lat=Double.parseDouble((String) j1.get("lat"));
            point = new Point();
            point.setLng(lng);
            point.setLat(lat);
            }

        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RuntimeException(e);
        }

        return point;
    }

}
