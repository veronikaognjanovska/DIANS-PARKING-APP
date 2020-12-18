package parkingfinder.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parkingfinder.model.ParkingSpot;
import parkingfinder.service.ParkingSpotsFilterService;

import java.util.List;

@RestController
@RequestMapping("/parking/filter")
public class ParkingSpotsFilterController {

    @Autowired
    ParkingSpotsFilterService parkingSpotsFilterService;

    @GetMapping("/all")
    public List<ParkingSpot> filterParkingsAll() {
        return parkingSpotsFilterService.getParkingSpotsAll();
    }

    @GetMapping("/access")
    public List<ParkingSpot> filterParkingByAccess(@RequestParam String q) {
        return parkingSpotsFilterService.getParkingSpotsByAccess(q);
    }

    @GetMapping("/type")
    public List<ParkingSpot> filterParkingByType(@RequestParam String q) {
        return parkingSpotsFilterService.getParkingSpotsByType(q);
    }

}
