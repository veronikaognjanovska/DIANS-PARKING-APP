package parkingapp.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class StreetName {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int ID;

    private String streetName;

}
