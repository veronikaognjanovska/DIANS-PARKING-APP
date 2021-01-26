package com.parkingfinder.parkingservice.service;

import com.parkingfinder.parkingservice.model.ParkingSpot;
import com.parkingfinder.parkingservice.repository.ParkingSpotRepository;
import com.parkingfinder.parkingservice.util.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void initMocks(){
    }

    private ParkingSpot createParkingSpot(String name) {
        ParkingSpot parkingSpot =  new ParkingSpot();
        parkingSpot.setName(name);
        return parkingSpot;
    }

    private void testCaseLoadDataFromDatabasePositive() {
        List<ParkingSpot> staticList = new ArrayList<>();
        staticList.add(createParkingSpot("name"));
        staticList.add(new ParkingSpot());

        when(mockParkingSpotRepository.findAll())
                .thenReturn(staticList);
        parkingSpotDataLoader.loadDataFromDatabase();

        int loadedParkingSpots = parkingSpotDataLoader.getParkingSpotsCount();
        assertTrue(loadedParkingSpots>0);
        assertEquals(1, loadedParkingSpots);
    }

    private void testCaseLoadDataFromDatabaseNegative() {
        List<ParkingSpot> staticList = new ArrayList<>();

        when(mockParkingSpotRepository.findAll())
                .thenReturn(staticList);
        parkingSpotDataLoader.loadDataFromDatabase();

        int loadedParkingSpots = parkingSpotDataLoader.getParkingSpotsCount();
        assertEquals(0, loadedParkingSpots);

    }

    @Test
    public void shouldLoadParkingDataFromDatabase() {
        testCaseLoadDataFromDatabasePositive();
        testCaseLoadDataFromDatabaseNegative();
    }

    public Observer createObserver() {
        return subject -> { };
    }

    @Test
    public void shouldRegisterObserver() {
        parkingSpotDataLoader.registerObserver(createObserver());
        assertEquals(1, parkingSpotDataLoader.getObserversCount());
        parkingSpotDataLoader.registerObserver(createObserver());
        assertEquals(2, parkingSpotDataLoader.getObserversCount());
    }

    @Test
    public void shouldRemoveObservers() {
        Observer observer = createObserver();
        parkingSpotDataLoader.removeObserver(observer);
        assertEquals(0, parkingSpotDataLoader.getObserversCount());
        parkingSpotDataLoader.registerObserver(observer);
        assertEquals(1, parkingSpotDataLoader.getObserversCount());
        parkingSpotDataLoader.removeObserver(observer);
        assertEquals(0, parkingSpotDataLoader.getObserversCount());
    }
}