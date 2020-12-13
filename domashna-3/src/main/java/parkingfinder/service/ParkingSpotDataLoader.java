package parkingfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import parkingfinder.model.ParkingSpot;
import parkingfinder.repository.ParkingSpotRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@EnableAsync
public class ParkingSpotDataLoader {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    private List<ParkingSpot> parkingSpotsAll = new ArrayList<>();

    @PostConstruct
    private void init(){
        readDataFromRepository();
    }

    @Async
    @Scheduled(cron = "0 1 1 * * ?")
    public void loadData() {
        readDataFromRepository();
    }

    protected void readDataFromRepository() {
        parkingSpotsAll = StreamSupport
                .stream(parkingSpotRepository.findAll().spliterator(), false)
                .filter(parkingSpot -> parkingSpot.getName()!=null && !parkingSpot.getName().isEmpty())
                .collect(Collectors.toList());
        parkingSpotsAll.stream()
                .filter(parkingSpot -> parkingSpot.getParking_type()==null || parkingSpot.getParking_type().isEmpty())
                .forEach(parkingSpot -> parkingSpot.setParking_type("surface"));
        parkingSpotsAll.stream()
                .filter(parkingSpot -> parkingSpot.getAccess()==null || parkingSpot.getAccess().isEmpty())
                .forEach(parkingSpot -> parkingSpot.setAccess("public"));
    }

    protected int getParkingSpotsCount() {
       return parkingSpotsAll.size();
    }
}
