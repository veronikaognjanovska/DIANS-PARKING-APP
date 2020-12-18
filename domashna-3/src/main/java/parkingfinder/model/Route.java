package parkingfinder.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Data

public class Route{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer ID;

    // USER ???? BRANCH MERGE FIRST
     @ManyToOne
     private User userId;

    @ManyToMany
    private List<Point> points;

    @ManyToMany
    private Set<StreetName> streetNames;

    private ZonedDateTime timestamp;

    public Route() {
        this.points=new LinkedList<>();
        this.streetNames=new LinkedHashSet<>();
    }
}