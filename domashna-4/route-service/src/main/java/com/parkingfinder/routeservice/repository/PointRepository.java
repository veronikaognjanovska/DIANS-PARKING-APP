package com.parkingfinder.routeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkingfinder.routeservice.model.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {
}
