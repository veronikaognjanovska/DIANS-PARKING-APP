package com.parkingfinder.routeservice.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.parkingfinder.routeservice.model.Point;

@Service
public class PointService extends BaseService<Point>{


    public Point getPointFromName(String q) {
        String search = q.replace(" ","-");
        String url = "https://nominatim.openstreetmap.org/search?q="+search+"&limit=1&format=json";
        return this.findGeoObject(url);
    }

    @Override
    protected Point parseJson(Object objectJson) {
        Point point = null;
        try {
            JSONArray json=(JSONArray) objectJson;
            point = new Point();
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

    @Override
    protected Object restTemplateExchange(String s) throws ParseException {
        JSONArray json=null;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(s, String.class);
        String response = responseEntity.getBody();
        JSONParser parser = new JSONParser();
        json = (JSONArray) parser.parse(response);
        return json;
    }
}
