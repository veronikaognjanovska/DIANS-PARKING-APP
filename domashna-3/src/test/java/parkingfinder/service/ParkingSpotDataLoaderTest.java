package parkingfinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import parkingfinder.model.ParkingSpot;
import parkingfinder.repository.ParkingSpotRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDataLoaderTest {

    @Mock
    private ParkingSpotRepository mockParkingSpotRepository;

    @InjectMocks
    private ParkingSpotDataLoader parkingSpotDataLoader;

    @Mock
    private ParkingSpotsFilterService parkingSpotsFilterService;

    @BeforeEach
    void initMocks(){
    }

    @Test
    public void shouldLoadParkingDataFromDatabase() {
        //data
        List<ParkingSpot> staticList = new ArrayList<>();
        ParkingSpot parkingSpot =  new ParkingSpot();
        parkingSpot.setName("name");
        staticList.add(parkingSpot);
        staticList.add(new ParkingSpot());

        //when then
        when(mockParkingSpotRepository.findAll())
                .thenReturn(staticList);
        parkingSpotDataLoader.readDataFromRepository();
        
        //assert
        int loadedParkingSpots = parkingSpotDataLoader.getParkingSpotsCount();
        assertTrue(loadedParkingSpots>0);
        assertEquals(1, loadedParkingSpots);
        //data
        staticList.clear();

        //when then
        when(mockParkingSpotRepository.findAll())
                .thenReturn(staticList);
        parkingSpotDataLoader.readDataFromRepository();

        //assert
        loadedParkingSpots = parkingSpotDataLoader.getParkingSpotsCount();
        assertEquals(0, loadedParkingSpots);

    }
}