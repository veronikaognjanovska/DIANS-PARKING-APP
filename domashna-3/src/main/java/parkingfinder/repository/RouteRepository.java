package parkingfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingfinder.model.Route;

@Repository
public interface RouteRepository  extends JpaRepository<Route, Integer> {

}
