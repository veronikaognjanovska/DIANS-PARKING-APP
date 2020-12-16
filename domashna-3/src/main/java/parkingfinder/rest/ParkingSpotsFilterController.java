package parkingfinder.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parkingfinder.model.ParkingSpot;
import parkingfinder.service.ParkingSpotsFilterService;

import java.util.List;

@RestController
public class ParkingSpotsFilterController {

    @Autowired
    ParkingSpotsFilterService parkingSpotsFilterService;

    @GetMapping("/parking/filter/all")
    public List<ParkingSpot> filterParkingsAll() {
        return parkingSpotsFilterService.getParkingSpotsAll();
    }

    @GetMapping("/parking/filter/access")
    public List<ParkingSpot> filterParkingByAccess(@RequestParam String access) {
        return parkingSpotsFilterService.getParkingSpotsByAccess(access);
    }

    @GetMapping("/parking/filter/type")
    public List<ParkingSpot> filterParkingByType(@RequestParam String type) {
        return parkingSpotsFilterService.getParkingSpotsByType(type);
    }

}
