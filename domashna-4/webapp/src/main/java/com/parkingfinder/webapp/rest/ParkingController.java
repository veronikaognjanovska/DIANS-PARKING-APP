package com.parkingfinder.webapp.rest;

import com.parkingfinder.webapp.dtos.ParkingDto;
import com.parkingfinder.webapp.service.ParkingSpotsFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest controller for fetching filtered parking spots
 * */
@RestController
@RequestMapping("/parking/filter")
public class ParkingController {
    @Autowired
    private ParkingSpotsFetchService parkingSpotsFetchService;

    /**
     * Returns all parking spots not filtered
     * @return List - list of parking spots, empty list if an exception occured
     * */
    @GetMapping("/all")
    public List<ParkingDto> filterParkingsAll() {
        try{
            return parkingSpotsFetchService.getParkingSpotsAll();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    /**
     * Returns all parking spots filtered by access type
     * @return List - list of parking spots, empty list if an exception occured
     * */
    @GetMapping("/access")
    public List<ParkingDto> filterParkingsByAccess(@RequestParam String accessLevel) {
        try{
            return parkingSpotsFetchService.getParkingByAccessLevel(accessLevel);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    /**
     * Returns all parking spots filtered by parking type
     * @return List - list of parking spots, empty list if an exception occured
     * */
    @GetMapping("/type")
    public List<ParkingDto> filterParkingsByType(@RequestParam String parkingType) {
        try{
            return parkingSpotsFetchService.getParkingByType(parkingType);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

}
