package parkingfinder.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkingfinder.model.Point;

@Service
public class PointService {

    private  RestTemplate restTemplate;

    public PointService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    protected JSONArray sentRequestArray(String url){
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
    // http://localhost:8080/api/name?q=New York City

    public Point getPointFromName(String q) {

        String url = "https://nominatim.openstreetmap.org/search?q=%22+q+%22&limit=1&format=json";
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
