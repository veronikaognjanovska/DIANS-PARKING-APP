package parkingfinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import parkingfinder.model.Point;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private PointService pointService;

    @BeforeEach
    private void initMocks() {

    }
    @Test
    public void shouldReturnPoint()
    {
        String s1 ="[{'lat': '-14.7067141','lon': '-75.1308498'}]";

        ResponseEntity<Object> rs=new ResponseEntity<>(s1,HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(),any())).thenReturn(rs);
        Point p1=pointService.getPointFromName("Pintija");
        assertNotNull(p1);

        assertEquals(-14.7067141,p1.getLat());
        assertEquals(-75.1308498,p1.getLng());

//        String s2 ="[{'lat': '-14.7067141','lon': '-75.1308498'}]";
//
//        ResponseEntity<Object> rs=new ResponseEntity<>(s1,HttpStatus.OK);
//        when(restTemplate.getForEntity(anyString(),any())).thenReturn(rs);
//        Point p1=pointService.getPointFromName("Pintija");
//        assertNotNull(p1);
//
//        assertEquals(-14.7067141,p1.getLat());
//        assertEquals(-75.1308498,p1.getLng());

    }








}