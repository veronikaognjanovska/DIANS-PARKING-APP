package parkingapp.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import parkingapp.app.model.ParkingSpot;
import parkingapp.app.repository.ParkingSpotRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@EnableAsync
public class ParkingSpotDataLoader {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    ParkingSpotsFilterService parkingSpotsFilterService;

    @PostConstruct
    private void init(){
        readDataFromRepository();
    }

    @Async
    @Scheduled(cron = "0 1 1 * * ?")
    public void loadData() {
        readDataFromRepository();
    }

    private void readDataFromRepository() {
        List<ParkingSpot> parkingSpotsAll = StreamSupport
                .stream(parkingSpotRepository.findAll().spliterator(), false)
                .filter(parkingSpot -> parkingSpot.getName()!=null && !parkingSpot.getName().isEmpty())
                .collect(Collectors.toList());
        parkingSpotsAll.stream()
                .filter(parkingSpot -> parkingSpot.getParking_type()==null || parkingSpot.getParking_type().isEmpty())
                .forEach(parkingSpot -> parkingSpot.setParking_type("surface"));
        parkingSpotsAll.stream()
                .filter(parkingSpot -> parkingSpot.getAccess()==null || parkingSpot.getAccess().isEmpty())
                .forEach(parkingSpot -> parkingSpot.setAccess("public"));

        parkingSpotsFilterService.setParkingSpotsAll(parkingSpotsAll);

    }

}
