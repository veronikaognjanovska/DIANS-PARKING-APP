package parkingfinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import parkingfinder.model.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotsFilterServiceTest {

    private ParkingSpotsFilterService parkingSpotsFilterService;
    private List<ParkingSpot> staticParkingList;

    @BeforeEach
    private void initMocks() {
        staticParkingList = new ArrayList<>();
        staticParkingList.add(createParkingSpotStaticData("ps1", "public", "surface"));
        staticParkingList.add(createParkingSpotStaticData("ps2", "private", "surface"));
        staticParkingList.add(createParkingSpotStaticData("ps3", "customer", "multi-story"));

        parkingSpotsFilterService = new ParkingSpotsFilterService();
        parkingSpotsFilterService.setParkingSpotsAll(staticParkingList);
    }

    private ParkingSpot createParkingSpotStaticData(String name, String access, String type) {
        ParkingSpot ps1 = new ParkingSpot();
        ps1.setName(name);
        ps1.setAccess(access);
        ps1.setParking_type(type);
        return ps1;
    }

    @Test
    void shouldReturnAllParkingSpots() {
        //assert
        assertEquals(staticParkingList, parkingSpotsFilterService.getParkingSpotsAll());

        //data
        staticParkingList = null;
        parkingSpotsFilterService.setParkingSpotsAll(staticParkingList);

        //assert not set
        assertEquals(3, parkingSpotsFilterService.getParkingSpotsAll().size());

        //data
        staticParkingList = new ArrayList<>();
        parkingSpotsFilterService = new ParkingSpotsFilterService();
        parkingSpotsFilterService.setParkingSpotsAll(staticParkingList);

        //assert
        assertEquals(0, parkingSpotsFilterService.getParkingSpotsAll().size());
    }

    @Test
    void shouldReturnAllParkingSpotsWithSpecifiedAccessLevel() {
        //assert
        List<ParkingSpot> parkingsWithPublicAccess = parkingSpotsFilterService.getParkingSpotsByAccess("public");
        assertEquals(staticParkingList.get(0), parkingsWithPublicAccess.get(0));
        assertEquals(1, parkingsWithPublicAccess.size());

        List<ParkingSpot> parkingSpotsWithPrivateAccess = parkingSpotsFilterService.getParkingSpotsByAccess("private");
        assertEquals(staticParkingList.get(1), parkingSpotsWithPrivateAccess.get(0));
        assertEquals(1, parkingSpotsWithPrivateAccess.size());

        List<ParkingSpot> parkingSpotsWithCustomerAccess = parkingSpotsFilterService.getParkingSpotsByAccess("customer");
        assertEquals(staticParkingList.get(2), parkingSpotsWithCustomerAccess.get(0));
        assertEquals(1, parkingSpotsWithCustomerAccess.size());

        List<ParkingSpot> parkingSpotsAccessNull = parkingSpotsFilterService.getParkingSpotsByAccess(null);
        assertEquals(staticParkingList, parkingSpotsAccessNull);
        assertEquals(3, parkingSpotsAccessNull.size());

        List<ParkingSpot> parkingSpotsAccessRandom = parkingSpotsFilterService.getParkingSpotsByAccess("shbahfsbfjas");
        assertEquals(staticParkingList, parkingSpotsAccessNull);
        assertEquals(3, parkingSpotsAccessRandom.size());

        List<ParkingSpot> parkingSpotsAccessEmpty = parkingSpotsFilterService.getParkingSpotsByAccess("");
        assertEquals(staticParkingList, parkingSpotsAccessNull);
        assertEquals(3, parkingSpotsAccessEmpty.size());
    }

    @Test
    void shouldReturnAllParkingSpotsOfSpecifiedType() {
        List<ParkingSpot> parkingSpotsTypeSurface = parkingSpotsFilterService.getParkingSpotsByType("surface");
        assertEquals(2, parkingSpotsTypeSurface.size());
        assertEquals(staticParkingList.get(0), parkingSpotsTypeSurface.get(0));

        List<ParkingSpot> parkingSpotsTypeMultiStory = parkingSpotsFilterService.getParkingSpotsByType("multi-story");
        assertEquals(1, parkingSpotsTypeMultiStory.size());
        assertEquals(staticParkingList.get(2), parkingSpotsTypeMultiStory.get(0));

        List<ParkingSpot> parkingSpotsTypeNull = parkingSpotsFilterService.getParkingSpotsByType(null);
        assertEquals(staticParkingList, parkingSpotsTypeNull);
        assertEquals(3, parkingSpotsTypeNull.size());

        List<ParkingSpot> parkingSpotsTypeRandom = parkingSpotsFilterService.getParkingSpotsByType("gaghnahjgnakjnas");
        assertEquals(staticParkingList, parkingSpotsTypeRandom);
        assertEquals(3, parkingSpotsTypeRandom.size());

        List<ParkingSpot> parkingSpotsTypeEmpty = parkingSpotsFilterService.getParkingSpotsByType("");
        assertEquals(staticParkingList, parkingSpotsTypeEmpty);
        assertEquals(3, parkingSpotsTypeEmpty.size());
    }
}