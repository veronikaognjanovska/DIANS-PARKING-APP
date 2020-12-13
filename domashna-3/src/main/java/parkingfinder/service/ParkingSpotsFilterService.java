package parkingfinder.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import parkingfinder.model.ParkingSpot;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@NoArgsConstructor
public class ParkingSpotsFilterService {

    private List<ParkingSpot> parkingSpotsAll = new ArrayList<>();
    private Map<String, List<ParkingSpot>> parkingSpotsByAccess = new HashMap<>();
    private Map<String, List<ParkingSpot>> parkingSpotsByType = new HashMap<>();

    @PostConstruct
    private void init() {
        loadData();
    }

    private void loadData() {
        parkingSpotsByAccess = parkingSpotsAll.stream()
                .collect(groupingBy(ParkingSpot::getAccess));
        parkingSpotsByType = parkingSpotsAll.stream()
                .collect(groupingBy(ParkingSpot::getParking_type));
    }

    public List<ParkingSpot> getParkingSpotsAll(){
        return parkingSpotsAll;
    }

    public List<ParkingSpot> getParkingSpotsByAccess(String access) {
        return parkingSpotsByAccess.getOrDefault(access, parkingSpotsAll);
    }

    public List<ParkingSpot> getParkingSpotsByType(String type) {
        return parkingSpotsByType.getOrDefault(type, parkingSpotsAll);
    }

    public void setParkingSpotsAll(List<ParkingSpot> parkingSpots) {
        if (parkingSpots != null) {
            parkingSpotsAll = parkingSpots;
            loadData();
        }
    }
}
