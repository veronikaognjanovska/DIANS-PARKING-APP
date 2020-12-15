package parkingfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingfinder.model.StreetName;

@Repository
public interface StreetNameRepository  extends JpaRepository<StreetName, Integer> {
}
