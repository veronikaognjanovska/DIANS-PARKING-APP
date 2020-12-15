package parkingfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import parkingfinder.model.ParkingSpot;
import parkingfinder.service.ParkingSpotsFilterService;


import java.util.List;


@Controller
public class HomeController {

    @Autowired
    ParkingSpotsFilterService service;
    @GetMapping("/")
    public String home(Model model){

        List<ParkingSpot> parkingSpotList = service.getParkingSpotsAll();

        model.addAttribute("allParkings", parkingSpotList);


        return "home.html";

    }

}
