package com.parkingfinder.routeservice.service;

import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class BaseService <T> {

    @Autowired
    protected RestTemplate restTemplate ;

    public final T findGeoObject (String url){
        Object jsonObject = this.sentRequestObject(url);
        T geoObject= this.parseJson(jsonObject);
        this.saveToDatabase(geoObject);
        return geoObject;
    }



    protected UriComponentsBuilder getUriComponentsBuilder(String url){
        return UriComponentsBuilder.fromHttpUrl(url);
    }

    protected Object sentRequestObject(String url){
        UriComponentsBuilder uriBuilder=this.getUriComponentsBuilder(url);

        Object json=null;
        try {
            json = this.restTemplateExchange(uriBuilder.toUriString());
        }catch(Exception e){
            //
            //Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            //logger.config("Exception");
            throw new RuntimeException(e);
        }
        return json;
    }




    protected void saveToDatabase(T geoObject){}
    protected abstract T parseJson(Object json);
    protected abstract Object restTemplateExchange(String s) throws ParseException;


}
