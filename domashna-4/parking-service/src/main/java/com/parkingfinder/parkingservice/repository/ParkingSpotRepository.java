package com.parkingfinder.parkingservice.repository;

import com.parkingfinder.parkingservice.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
* Repository for manipulating with parking spots model from database
* */
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, String> {

}
