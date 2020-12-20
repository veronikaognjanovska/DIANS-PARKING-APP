package parkingfinder.rest;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.*;
import parkingfinder.model.Point;
import parkingfinder.service.PointService;

import java.time.Duration;

@RestController
@RequestMapping("/point")
public class PointFinderController {
    public final PointService pointService;
    private Bucket bucket;

    public PointFinderController(PointService pointService)
    {
        Bandwidth limit = Bandwidth.classic(200, Refill.greedy(200, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        this.pointService=pointService;
    }

    //http://localhost:8080/point?q=New York City
    //http://localhost:8080/point?q=Skopje
  @GetMapping
    public Point getPointFromName(@RequestParam String q)
    {
        if(bucket.tryConsume(1)) {
            return this.pointService.getPointFromName(q);
        }
        return new Point();
    }
}

