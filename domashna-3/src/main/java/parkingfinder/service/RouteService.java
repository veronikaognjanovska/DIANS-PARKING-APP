package parkingfinder.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkingfinder.model.Point;
import parkingfinder.model.Route;
import parkingfinder.model.StreetName;
import parkingfinder.repository.RouteRepository;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository repository;

    public RouteService(RouteRepository repository) {
        this.repository = repository;
    }

//    public List<Route> findHistoryRoutes(User user){
//        
//    }


    public Route findRoute (String type, Double lng1, Double lan1, Double lng2, Double lan2){
        String url = "https://routing.openstreetmap.de/routed-bike/route/v1/"+type+"/" +
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



}
