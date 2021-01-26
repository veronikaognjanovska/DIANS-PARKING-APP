package com.parkingfinder.parkingservice.service;

import com.parkingfinder.parkingservice.model.ParkingSpot;
import com.parkingfinder.parkingservice.repository.ParkingSpotRepository;
import com.parkingfinder.parkingservice.util.Constants;
import com.parkingfinder.parkingservice.util.DataLoader;
import com.parkingfinder.parkingservice.util.Observer;
import com.parkingfinder.parkingservice.util.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* Component with scheduled job for reading database updates of parking spots
* on a fixed delay.
* Implements the Subject interface and notifies observers when new data has been
* read from database
* @author Milena Trajanoska
* */
@Component
@EnableAsync
public class ParkingSpotDataLoader implements Subject, DataLoader {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    private final List<Observer> observers = new ArrayList<>();

    private List<ParkingSpot> parkingSpotsAll = new ArrayList<>();

    /**
    * Asynchronous scheduled job
    * Reads parking spots database every 30 minutes and loads all parking data
    * Replaces empty values with default constants
    * */
    @Async
    @Scheduled(fixedDelay = 1800000)
    public void loadDataFromDatabase() {
        parkingSpotsAll = parkingSpotRepository.findAll().stream()
                .filter(parkingSpot -> !checkEmptyStringOrNull(parkingSpot.getName()))
                .map(this::mapParkingSpotWithDefaultValues)
                .collect(Collectors.toList());
        notifyObservers();
    }

    private ParkingSpot mapParkingSpotWithDefaultValues(ParkingSpot parkingSpot) {
        if(checkEmptyStringOrNull(parkingSpot.getParking_type())){
            parkingSpot.setParking_type(Constants.DEFAULT_PARKING_TYPE);
        }
        if(checkEmptyStringOrNull(parkingSpot.getAccess())){
            parkingSpot.setAccess(Constants.DEFAULT_ACCESS_LEVEL);
        }
        return parkingSpot;
    }

    private boolean checkEmptyStringOrNull(String string) {
        return string==null || string.isEmpty();
    }

    /**
    * Method that returns the number of parking spots read from database
    * @return int - number of parking spots
    * */
    protected int getParkingSpotsCount() {
       return parkingSpotsAll.size();
    }

    /**
     * Method that returns the number of observers to the data loader
     * @return int - number of observers
     * */
    protected int getObserversCount() {
        return observers.size();
    }

    /**
    * Method that returns a list of all the parking spots read from database
    * @return List - list of all parking spots
    * */
    public List<ParkingSpot> getParkingSpotsAll() {
        return this.parkingSpotsAll;
    }

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(o -> o.update(this));
    }
}
