package parkingapp.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import parkingapp.app.model.ParkingSpot;
import parkingapp.app.service.ParkingSpotsFilterService;

import java.util.List;

@Controller
class HomeController{

    @Autowired
    ParkingSpotsFilterService service;
    @GetMapping("/")
    public String home(Model model){

        List<ParkingSpot> parkingSpotList = service.getParkingSpotsAll();
        model.addAttribute("allParkings", parkingSpotList);
        return "home.html"; //fajlot na monika koga kje go stavi

    }

    /*
    @GetMapping("/user") //podocna da bide zameneto so @GetMapping("/get/user/{id}")
    public String user(){

        return null;
        //return user.html //kje go napravam
    }

     */
}