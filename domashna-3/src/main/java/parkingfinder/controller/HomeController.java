package parkingfinder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import parkingfinder.model.ParkingSpot;
import parkingfinder.repository.ParkingSpotRepository;
import parkingfinder.service.ParkingSpotsFilterService;


import java.util.List;


@Controller
public class HomeController {

    @Autowired
    ParkingSpotsFilterService service;

    @Autowired
    ParkingSpotRepository repository;

    @GetMapping("/")
    public String home(Model model){

        ObjectMapper objectMapper = new ObjectMapper();

        List<ParkingSpot> parkingSpotList = service.getParkingSpotsAll();
        try {
            model.addAttribute("allParkings", objectMapper.writeValueAsString(parkingSpotList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "home"; //fajlot na monika koga kje go stavi

    }

    @GetMapping("/parking")
    public String parkingDetails(String id, Model model) {
        ParkingSpot parkingSpot = repository.findById(id).get();
        model.addAttribute("parking", parkingSpot);
        return "parking-details";
    }

}
