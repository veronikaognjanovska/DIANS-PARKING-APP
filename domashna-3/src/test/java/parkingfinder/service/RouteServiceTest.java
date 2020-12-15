package parkingfinder.service;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import parkingfinder.model.ParkingSpot;
import parkingfinder.repository.ParkingSpotRepository;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

//    @Mock
//    private RouteService routeService;
//
//    @Mock
//    private RouteRepository routeRepository;
//
////    @InjectMocks
////    private RouteService routeService;
//
//
//
//    @BeforeEach
//    void initMocks(){
//    }
//
//    @Test
//    public void shouldLoadRouteDataFromDatabase() {
//        //data
//        List<Route> staticList = new ArrayList<>();
//        Route route =  new Route();
//        route.setID(1);
//        staticList.add(route);
//        staticList.add(new Route());
//
//        //when then
//        when(routeRepository.findAll())
//                .thenReturn(staticList);
////        parkingSpotDataLoader.readDataFromRepository();
//
//        //assert
//        int loadedParkingSpots = parkingSpotDataLoader.getParkingSpotsCount();
//        assertTrue(loadedParkingSpots>0);
//        assertEquals(1, loadedParkingSpots);
//        //data
//        staticList.clear();
//
//        //when then
//        when(mockParkingSpotRepository.findAll())
//                .thenReturn(staticList);
//        parkingSpotDataLoader.readDataFromRepository();
//
//        //assert
//        loadedParkingSpots = parkingSpotDataLoader.getParkingSpotsCount();
//        assertEquals(0, loadedParkingSpots);

//    }
}
