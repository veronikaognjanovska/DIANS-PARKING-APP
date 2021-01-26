package com.parkingfinder.routeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkingfinder.routeservice.model.StreetName;

@Repository
public interface StreetNameRepository  extends JpaRepository<StreetName, Integer> {
}
