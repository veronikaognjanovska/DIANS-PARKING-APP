package parkingfinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import parkingfinder.model.Route;
import parkingfinder.repository.PointRepository;
import parkingfinder.repository.RouteRepository;
import parkingfinder.repository.StreetNameRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteEvictionServiceTest {

    @Mock
    RouteRepository mockRouteRepository;

    @Mock
    PointRepository mockPointRepository;

    @Mock
    StreetNameRepository mockStreetNameRepository;

    @InjectMocks
    RouteEvictionService routeEvictionService;

    @BeforeEach
    public void init() {
        when(mockRouteRepository.findAll()).thenReturn(createStaticList(0, 10, 7));
        doNothing().when(mockRouteRepository).deleteAll(anyIterable());
    }

    public List<Route> createStaticList(int days1, int days2, int days3) {
        List<Route> staticList = new ArrayList<>();
        Route route1 = new Route();
        route1.setTimestamp(ZonedDateTime.now().minusDays(days1));
        staticList.add(route1);
        Route route2 = new Route();
        route2.setTimestamp(ZonedDateTime.now().minusDays(days2));
        staticList.add(route2);
        Route route3 = new Route();
        route3.setTimestamp(ZonedDateTime.now().minusDays(days3));
        staticList.add(route3);
        return staticList;
    }

    @Test
    public void shouldReturnNumberOfRoutesDeleted() {

        int deleted = routeEvictionService.evictOldRoutes();
        assertEquals(1, deleted);

        when(mockRouteRepository.findAll()).thenReturn(createStaticList(1, 2, 3));
        deleted = routeEvictionService.evictOldRoutes();
        assertEquals(0, deleted);

        when(mockRouteRepository.findAll()).thenReturn(createStaticList(8, 9, 13));
        deleted = routeEvictionService.evictOldRoutes();
        assertEquals(3, deleted);

        Route route1 = new Route();
        Route route2 = new Route();
        when(mockRouteRepository.findAll()).thenReturn(Arrays.asList(route1, route2));
        deleted = routeEvictionService.evictOldRoutes();
        assertEquals(0, deleted);
    }

}