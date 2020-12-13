package parkingfinder.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "parking")
@Data
@NoArgsConstructor
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    @NonNull
    private double lat;
    @NonNull
    private double lng;
    private String name;
    private Integer capacity;
    private String access;
    private String fee;
    private String operator;
    private String website;
    private String supervised;
    private String parking_type;

}
