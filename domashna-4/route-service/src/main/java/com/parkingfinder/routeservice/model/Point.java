package com.parkingfinder.routeservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Point database model
 * Model for retrieving and persisting location points to database
 **/
@Entity
@Data
@NoArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @NonNull
    private Double lat;

    @NonNull
    private Double lng;
}