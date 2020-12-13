package parkingfinder.repository;

import org.springframework.data.repository.CrudRepository;
import parkingfinder.model.ParkingSpot;

public interface ParkingSpotRepository extends CrudRepository<ParkingSpot, String> {

}
