package parkingapp.app.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int ID;

    @NonNull
    private double lat;
    @NonNull
    private double lng;

}
