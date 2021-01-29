package com.parkingfinder.routeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkingfinder.routeservice.model.StreetName;

/**
 * Repository for manipulating with street name model from database
 **/
@Repository
public interface StreetNameRepository  extends JpaRepository<StreetName, Integer> {
}
