package com.parkingfinder.routeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkingfinder.routeservice.model.Route;
import java.util.List;

/**
 * Repository for manipulating with route model from database
 **/
@Repository
public interface RouteRepository  extends JpaRepository<Route, Integer> {
    List<Route> findAllByUserEmail(String userEmail);
}
