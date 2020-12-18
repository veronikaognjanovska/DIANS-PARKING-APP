package parkingfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingfinder.model.Route;
import parkingfinder.model.User;

import java.util.List;

@Repository
public interface RouteRepository  extends JpaRepository<Route, Integer> {
    List<Route> findAllByUserId(User user);
}
