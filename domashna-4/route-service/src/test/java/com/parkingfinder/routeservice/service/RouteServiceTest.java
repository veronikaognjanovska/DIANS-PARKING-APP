package com.parkingfinder.routeservice.service;

import com.parkingfinder.routeservice.constants.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.parkingfinder.routeservice.model.*;
import com.parkingfinder.routeservice.model.exception.RouteNotFoundException;
import com.parkingfinder.routeservice.model.exception.UserNotFoundException;
import com.parkingfinder.routeservice.repository.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {
    @Mock
    protected RestTemplate restTemplate;
    @Mock
    protected RestTemplate restTemplateNoLoadBalanced;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private StreetNameRepository streetNameRepository;
    @InjectMocks
    private RouteService routeService;
    String url= Constants.ROUTE_URL_START_PART +"walking/21.455617,41.986311;21.455617,41.986311"+Constants.ROUTE_URL_END_PART;

    @Test
    public void Test() throws Exception{
        ResponseEntity<String> rs = new ResponseEntity<String>("User", HttpStatus.OK);
        String s1 = "{waypoints: [{name: Методија Шаторов - Шарло},{name: Методија Шаторов}]," +
                "routes: [{geometry: {coordinates: [[21.455617, 41.986311],[ 21.454922,41.986401]] }} ] }";
        this.shouldFetchRoutesAndStore(s1,url,rs,2,2,"Методија Шаторов - Шарло","Методија Шаторов");
        s1 = "{waypoints: []," +
                "routes: [{geometry: {coordinates: [[21.455617, 41.986311],[ 21.454922,41.986401]] }} ] }";
        this.shouldFetchRoutesAndStore(s1,url,rs,2,0,null,null);
    }
    private void shouldFetchRoutesAndStore(String s,String url,ResponseEntity<String>rs,Integer pointsSize,Integer streetNameSize,String name1,String name2) throws Exception{
        when(restTemplateNoLoadBalanced.getForObject(anyString(), any())).thenReturn(s);
        when(restTemplate.exchange(anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any()))
                .thenReturn(rs);
        Route r1 = routeService.findGeoObject(url);
        assertNotNull(r1);
        assertEquals(pointsSize, r1.getPoints().size());
        assertEquals(streetNameSize, r1.getStreetNames().size());
        if (streetNameSize!=0)
        {
            List<StreetName> strings = new ArrayList<>(r1.getStreetNames());
            assertEquals(name1, strings.get(0).getStreetName());
            assertEquals(name2, strings.get(1).getStreetName());
        }
    }

    @Test
    public void Test2() throws Exception{
        this.shouldReturnRouteNotFound("{}");
        this.shouldReturnRouteNotFound("null");
        this.shouldReturnRouteNotFound("{waypoints: [{name: Методија Шаторов - Шарло},{name: Методија Шаторов}]," +
                "routes: [{geometry: {}} ] }");
        this.shouldReturnRouteNotFound("{waypoints: [{name: Методија Шаторов - Шарло},{name: Методија Шаторов}]," +
                "routes: [{} ] }");
    }
    private void shouldReturnRouteNotFound(String s) throws Exception{
        when(restTemplateNoLoadBalanced.getForObject(anyString(), any())).thenReturn(s);
        try {
            routeService.findGeoObject(url);
        } catch (final RouteNotFoundException e) {
            final String msg = "Route was not found";
            assertEquals(msg, e.getMessage());
        }
    }
    @Test
    public void shouldFetchHistoryRoutes1() throws Exception {
        List<Route> list = fillList();
        when(routeRepository.findAllByUserEmail(any()))
                .thenReturn(list);
        List<Route> listRoute1 = routeService.findHistoryRoutes(anyString());
        assertNotNull(listRoute1);
        assertEquals(5, listRoute1.size());
    }
    @Test
    public void shouldThrowException() throws Exception {
        when(routeRepository.findAllByUserEmail(any()))
                .thenReturn(null);
        List<Route> listRoute2 = routeService.findHistoryRoutes(anyString());
        assertNotNull(listRoute2);
        assertEquals(0, listRoute2.size());
        try {
            List<Route> listRoute3 = routeService.findHistoryRoutes(null);
        } catch (final UserNotFoundException e) {
            final String msg = "User was not found";
            assertEquals(msg, e.getMessage());
        }
    }
    private List<Route> fillList() {
        List<Route> list = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Route route = new Route();
            list.add(route);
        }
        return list;
    }
}

