package com.parkingfinder.parkingservice.util;

/**
* Observer utility interface for implementing observer pattern
* */
public interface Observer {
    /**
    * Method for updating the observer when subject's state changes
    * Implements pull method
    * @param subject - Subject that the observer is registered with
    * */
    public void update(Subject subject);
}
