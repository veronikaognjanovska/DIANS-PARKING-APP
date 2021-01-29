package com.parkingfinder.routeservice.service;

import net.minidev.json.parser.ParseException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Abstract class for Template pattern
 * @author Veronika Ognjanovska and Veronika Stefanovska
 **/
public abstract class BaseService <T> {

    /**
     * Template method for finding geoObjects(routes or points) from given external service(url).
     * @param url - string that represents url for fetching data from external service
     * @return T - geoObject(route or point)
     **/
    public final T findGeoObject (String url){
        Object jsonObject = this.sentRequestObject(url);
        T geoObject= this.parseJson(jsonObject);
        this.saveToDatabase(geoObject);
        return geoObject;
    }

    /**
     * Method that returns UriComponentsBuilder from Http url.
     * @param url - string that represents url for fetching data from external service
     * @return UriComponentsBuilder
     **/
    protected UriComponentsBuilder getUriComponentsBuilder(String url){
        return UriComponentsBuilder.fromHttpUrl(url);
    }

    /**
     * Method that gets data from external service.
     * @param url - string that represents url for fetching data from external service
     * @return Object - JSON data from external service.
     **/
    protected Object sentRequestObject(String url){
        UriComponentsBuilder uriBuilder=this.getUriComponentsBuilder(url);
        Object json=null;
        try {
            json = this.restTemplateExchange(uriBuilder.toUriString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * Method that saves geoObject to database.
     * @param geoObject - geoObject(route or point)
     **/
    protected void saveToDatabase(T geoObject){}

    /**
     * Method that parses JSON data to geoObject.
     * @param json - object that represents JSON data
     * @return T - geoObject(route or point).
     **/
    protected abstract T parseJson(Object json);

    /**
     * Method that gets data from external service for specific geoObject.
     * @param s - URI string
     * @return Object - JSON data from external service.
     * @throws ParseException - Signals that an error has been reached unexpectedly while parsing.
     **/
    protected abstract Object restTemplateExchange(String s) throws ParseException;
}
