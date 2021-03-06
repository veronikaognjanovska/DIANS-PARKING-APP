package com.parkingfinder.routeservice.model;

import javax.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Route database model
 * Model for retrieving and persisting routes to database
 **/
@Entity
@Data
public class Route {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer ID;

    private String userEmail;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Point> points;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<StreetName> streetNames;

    private ZonedDateTime timestamp;

    public Route() {
        this.points=new LinkedList<>();
        this.streetNames=new LinkedHashSet<>();
    }
}
