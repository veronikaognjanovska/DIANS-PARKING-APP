package com.parkingfinder.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingfinder.webapp.dtos.ParkingDto;
import com.parkingfinder.webapp.service.ParkingSpotsFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    ParkingSpotsFetchService parkingSpotsFetchService;

    @GetMapping("/")
    public String home(Model model){

        ObjectMapper objectMapper = new ObjectMapper();

        List<ParkingDto> parkingSpotList = parkingSpotsFetchService.getParkingSpotsAll();
        try {
            model.addAttribute("allParkings", objectMapper.writeValueAsString(parkingSpotList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "home"; //fajlot na monika koga kje go stavi

    }

    @GetMapping("/parking")
    public String parkingDetails(String id, Model model) {
        ParkingDto parkingSpot = parkingSpotsFetchService.findById(id);
        model.addAttribute("parking", parkingSpot);
        return "parking-details";
    }

}
