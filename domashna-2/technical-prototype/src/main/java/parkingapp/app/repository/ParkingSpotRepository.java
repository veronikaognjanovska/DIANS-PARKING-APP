package parkingapp.app.repository;

import org.springframework.data.repository.CrudRepository;
import parkingapp.app.model.ParkingSpot;

public interface ParkingSpotRepository extends CrudRepository<ParkingSpot, String> {

}
