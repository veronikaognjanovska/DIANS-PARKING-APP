package com.parkingfinder.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingfinder.webapp.dtos.ParkingDto;
import com.parkingfinder.webapp.service.ParkingSpotsFetchService;
import com.parkingfinder.webapp.util.LocalBaseUrls;
import com.parkingfinder.webapp.util.URLPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for accessing front map home page and parking details page
 * \n Uses the ParkingSpotsFetchService
 * */
@Controller
public class HomeController {

    @Autowired
    private ParkingSpotsFetchService parkingSpotsFetchService;

    @Autowired
    private LocalBaseUrls localBaseUrls;

    /**
     * Returns the html page for the front map
     * @param model - Model object for setting parking spots attribute
     * */
    @GetMapping("/")
    public String home(Model model){

        ObjectMapper objectMapper = new ObjectMapper();
        List<ParkingDto> parkingSpotList = parkingSpotsFetchService.getParkingSpotsAll();
        try {
            model.addAttribute("allParkings", objectMapper.writeValueAsString(parkingSpotList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        model.addAttribute("parkingBaseUrl", localBaseUrls.getParkingService());
        model.addAttribute("routeBaseUrl", localBaseUrls.getRouteService());
        model.addAttribute("pointBaseUrl", localBaseUrls.getPointService());
        return "home";
    }

    /**
     * Returns the html page for a single parking spot based on the passed id
     * @param id - the id of the requested parking spot
     * @param model - Model object for setting parking attribute
     * */
    @GetMapping("/parking")
    public String parkingDetails(String id, Model model) {
        ParkingDto parkingSpot = parkingSpotsFetchService.findById(id);
        if(parkingSpot != null) {
            model.addAttribute("parking", parkingSpot);
            return "parking-details";
        }
        return "home";
    }

}
