package parkingfinder.rest;
import org.springframework.web.bind.annotation.*;
import parkingfinder.model.Point;
import parkingfinder.service.PointService;

@RestController
@RequestMapping("/point")
public class PointFinderController {
    public final PointService pointService;

    public PointFinderController(PointService pointService)
    {
        this.pointService=pointService;
    }

    //http://localhost:8080/point?q=New York City
    //http://localhost:8080/point?q=Skopje
  @GetMapping
    public Point getPointFromName(@RequestParam String q)
    {
        return this.pointService.getPointFromName(q);
    }
}

