package com.parkingfinder.parkingservice.service;

import com.parkingfinder.parkingservice.model.ParkingSpot;
import com.parkingfinder.parkingservice.util.Observer;
import com.parkingfinder.parkingservice.util.Subject;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
* In memory parking spots filter service
* Stores in memory filtered data about parking spots based on access level and parking type
* @author Milena Trajanoska
* */
@Service
@NoArgsConstructor
public class ParkingSpotsFilterService implements Observer {

    private List<ParkingSpot> parkingSpotsAll = new ArrayList<>();
    private Map<String, List<ParkingSpot>> parkingSpotsByAccess = new HashMap<>();
    private Map<String, List<ParkingSpot>> parkingSpotsByType = new HashMap<>();

    @Autowired
    private Subject parkingSpotDataLoader;

    @PostConstruct
    private void init() {
        parkingSpotDataLoader.registerObserver(this);
    }

    private void loadData() {
        parkingSpotsByAccess = parkingSpotsAll.stream()
                .collect(groupingBy(ParkingSpot::getAccess));
        parkingSpotsByType = parkingSpotsAll.stream()
                .collect(groupingBy(ParkingSpot::getParking_type));
    }

    /**
    * Method that returns a list of all the parking spots stored in memory
    * @return List<ParkingSpots> - list of all parking spots
    * */
    public List<ParkingSpot> getParkingSpotsAll(){
        return parkingSpotsAll;
    }

    /**
    * Method that returns a list of all parking spots that match a requested access level
    * @param access - string representing the required access level
    * @return List<ParkingSpot> - list of parking spots matching access level
    * */
    public List<ParkingSpot> getParkingSpotsByAccess(String access) {
        return parkingSpotsByAccess.getOrDefault(access, parkingSpotsAll);
    }

    /**
     * Method that returns a list of all parking spots that match a requested parking type
     * @param type - string representing the required parking type
     * @return List<ParkingSpot> - list of parking spots matching parking type
     * */
    public List<ParkingSpot> getParkingSpotsByType(String type) {
        return parkingSpotsByType.getOrDefault(type, parkingSpotsAll);
    }

    /**
    * Method for setting in memory parking spots
    * @param parkingSpots - list of ParkingSpot objects to be set
    * */
    public void setParkingSpotsAll(List<ParkingSpot> parkingSpots) {
        if (parkingSpots != null) {
            parkingSpotsAll = parkingSpots;
            loadData();
        }
    }

    @Override
    public void update(Subject subject) {
        ParkingSpotDataLoader dataLoader = (ParkingSpotDataLoader) subject;
        this.setParkingSpotsAll(dataLoader.getParkingSpotsAll());
        loadData();
    }
}
