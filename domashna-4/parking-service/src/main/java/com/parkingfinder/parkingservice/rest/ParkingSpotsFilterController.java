package com.parkingfinder.parkingservice.rest;

import com.parkingfinder.parkingservice.model.ParkingSpot;
import com.parkingfinder.parkingservice.service.ParkingSpotsFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* Rest controller for accessing parking spots data
 * @author Milena Trajanoska
* */
@RestController
@RequestMapping("/parking/filter")
public class ParkingSpotsFilterController {

    @Autowired
    ParkingSpotsFilterService parkingSpotsFilterService;

    /**
    * Method that returns all available parking spots from the database
    * @return List<ParkingSpot> - list of all the parking spots available
    * */
    @GetMapping("/all")
    public List<ParkingSpot> filterParkingsAll() {
        return parkingSpotsFilterService.getParkingSpotsAll();
    }

    /**
    * Method that returns a filtered result of parking
    * spots based on a user requested access level
    * @param accessLevel - string that represents the user requested access level
    * @return List<ParkingSpots> - a list of all parking spots
    * matching the requested access level
    * */
    @GetMapping("/access")
    public List<ParkingSpot> filterParkingByAccess(@RequestParam String accessLevel) {
        return parkingSpotsFilterService.getParkingSpotsByAccess(accessLevel);
    }

    /**
     * Method that returns a filtered result of parking
     * spots based on a user requested parking type
     * @param parkingType - string that represents the user requested parking type
     * @return List<ParkingSpots> - a list of all parking spots
     * matching the requested parking type
     * */
    @GetMapping("/type")
    public List<ParkingSpot> filterParkingByType(@RequestParam String parkingType) {
        return parkingSpotsFilterService.getParkingSpotsByType(parkingType);
    }

}
