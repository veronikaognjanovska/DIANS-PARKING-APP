package com.parkingfinder.routeservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Street name database model
 * Model for retrieving and persisting street names to database
 **/
@Entity
@Data
@NoArgsConstructor
public class StreetName{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer ID;

    private String streetName;
}