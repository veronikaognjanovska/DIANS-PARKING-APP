package parkingapp.app.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Route{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int ID;

    @OneToMany
    private List<Point> points;

    @OneToMany
    private List<String> streetNames;

}