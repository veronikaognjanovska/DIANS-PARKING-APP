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
    /**
     * Method that finds a list of routes searched by a user with the given email
     * @param userEmail
     * @return
     */
    List<Route> findAllByUserEmail(String userEmail);
}
