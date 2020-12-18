package parkingfinder.service;

import net.minidev.json.JSONObject;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.web.client.RestTemplate;
import parkingfinder.model.*;
import parkingfinder.model.exception.RouteNotFoundException;
import parkingfinder.model.exception.UserNotFoundException;
import parkingfinder.repository.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private StreetNameRepository streetNameRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RouteService routeService;


    @Test
    public void shouldFetchRoutesAndStoreThem() throws Exception{
        //when then
        String s ="{waypoints: [{name: Методија Шаторов - Шарло},{name: Методија Шаторов}]," +
                "routes: [{geometry: {coordinates: [[21.455617, 41.986311],[ 21.454922,41.986401]] }} ] }";
        when(restTemplate.getForObject(anyString(),any())).thenReturn(s);
        Route r1=routeService.findRoute("walking",21.455617, 41.986311,21.455617, 41.986311);
        assertNotNull(r1);
        assertEquals(2,r1.getPoints().size());
        assertEquals(2,r1.getStreetNames().size());
        List<StreetName>  strings = new ArrayList<>(r1.getStreetNames());
        assertEquals("Методија Шаторов - Шарло",strings.get(0).getStreetName());
        assertEquals("Методија Шаторов",strings.get(1).getStreetName());


        when(restTemplate.getForObject(anyString(),any())).thenReturn("{}");
        try
        {
            routeService.findRoute("walking",21.455617, 41.986311,21.455617, 41.986311);
        }
        catch( final RouteNotFoundException e )
        {
            final String msg = "Route was not found";
            assertEquals(msg, e.getMessage());
        }


        when(restTemplate.getForObject(anyString(),any())).thenReturn("");
        try
        {
            routeService.findRoute("walking",21.455617, 41.986311,21.455617, 41.986311);
        }
        catch( final RouteNotFoundException e )
        {
            final String msg = "Route was not found";
            assertEquals(msg, e.getMessage());
        }


        s ="{waypoints: []," +
        "routes: [{geometry: {coordinates: [[21.455617, 41.986311],[ 21.454922,41.986401]] }} ] }";
        when(restTemplate.getForObject(anyString(),any())).thenReturn(s);
        Route r2=routeService.findRoute("walking",21.455617, 41.986311,21.455617, 41.986311);
        assertNotNull(r2);
        assertEquals(2,r2.getPoints().size());
        assertEquals(0,r2.getStreetNames().size());


        s ="{waypoints: [{name: Методија Шаторов - Шарло},{name: Методија Шаторов}]," +
                "routes: [{geometry: {}} ] }";
        when(restTemplate.getForObject(anyString(),any())).thenReturn(s);
        try
        {
            routeService.findRoute("walking",21.455617, 41.986311,21.455617, 41.986311);
        }
        catch( final RouteNotFoundException e )
        {
            final String msg = "Route was not found";
            assertEquals(msg, e.getMessage());
        }


        s ="{waypoints: [{name: Методија Шаторов - Шарло},{name: Методија Шаторов}]," +
                "routes: [{} ] }";
        when(restTemplate.getForObject(anyString(),any())).thenReturn(s);
        try
        {
            routeService.findRoute("walking",21.455617, 41.986311,21.455617, 41.986311);
        }
        catch( final RouteNotFoundException e )
        {
            final String msg = "Route was not found";
            assertEquals(msg, e.getMessage());
        }
    }

    private List<Route> fillList(){
        List<Route> list= new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Route route =  new Route();
            list.add(route);
        }
        return list;
    }

    @Test
    public void shouldFetchHistoryRoutes() throws Exception{
        User user= new User();
        List<Route> list = fillList();


        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));
        when(routeRepository.findAllByUserId(any()))
                .thenReturn(list);
        List<Route> listRoute1 =  routeService.findHistoryRoutes(anyString());
        assertNotNull(listRoute1);
        assertEquals(5,listRoute1.size());


        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));
        when(routeRepository.findAllByUserId(any()))
                .thenReturn(null);
        List<Route> listRoute2 =  routeService.findHistoryRoutes(anyString());
        assertNotNull(listRoute2);
        assertEquals(0,listRoute2.size());

        

        try
        {
            List<Route> listRoute3 =  routeService.findHistoryRoutes(null);
        }
        catch( final UserNotFoundException e )
        {
            final String msg = "User was not found";
            assertEquals(msg, e.getMessage());
        }

    }



}

