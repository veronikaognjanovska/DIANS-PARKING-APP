package parkingfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingfinder.model.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {
}
