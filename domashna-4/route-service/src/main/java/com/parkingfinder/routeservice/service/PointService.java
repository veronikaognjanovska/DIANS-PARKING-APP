package com.parkingfinder.routeservice.service;

import com.parkingfinder.routeservice.constants.Constants;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.parkingfinder.routeservice.model.Point;
import org.springframework.web.client.RestTemplate;

/**
 * Point service for resolving geolocation from name
 * @author Veronika Stefanovska
 **/
@Service
public class PointService extends BaseService<Point>{
    @Autowired
    protected RestTemplate restTemplateNoLoadBalanced;

    /**
     * Method that parses JSON data to point.
     * @param objectJson - object that represents JSON data
     * @return Point.
     **/
    @Override
    protected Point parseJson(Object objectJson) {
        Point point = null;
        try {
            JSONArray json=(JSONArray) objectJson;
            point = new Point();
            if(!json.isEmpty()){
                point=this.getPoint((JSONObject)json.get(0));
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return point;
    }

    /**
     * Method that gets data from external service for specific location name.
     * @param s - URI string
     * @return Object - JSON data from external service.
     * @throws ParseException - Signals that an error has been reached unexpectedly while parsing.
     **/
    @Override
    protected Object restTemplateExchange(String s) throws ParseException {
        JSONArray json=null;
        ResponseEntity<String> responseEntity = restTemplateNoLoadBalanced.getForEntity(s, String.class);
        String response = responseEntity.getBody();
        JSONParser parser = new JSONParser();
        json = (JSONArray) parser.parse(response);
        return json;
    }

    private Point getPoint(JSONObject j1)
    {
        Double lng=Double.parseDouble((String) j1.get(Constants.LONGITUDE));
        Double lat=Double.parseDouble((String) j1.get(Constants.LATITUDE));
        Point point = new Point();
        point.setLng(lng);
        point.setLat(lat);
        return point;
    }
}
