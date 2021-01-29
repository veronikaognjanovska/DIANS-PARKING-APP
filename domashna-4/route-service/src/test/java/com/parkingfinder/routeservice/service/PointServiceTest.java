package com.parkingfinder.routeservice.service;

import com.parkingfinder.routeservice.constants.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.parkingfinder.routeservice.model.Point;
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
    private void initMocks(){}

    String urlOK = Constants.POINT_URL_START_PART +"Pintija"+Constants.POINT_URL_END_PART;
    String urlNotOK = Constants.POINT_URL_START_PART +"sfhajfshs"+Constants.POINT_URL_END_PART;
    @Test
    public void shouldReturnPoint()
    {
        String s1 ="[{'lat': '-14.7067141','lon': '-75.1308498'}]";
        ResponseEntity<Object> rs=new ResponseEntity<>(s1,HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(),any())).thenReturn(rs);
        Point p1=pointService.findGeoObject(urlOK);
        assertNotNull(p1);
        assertEquals(-14.7067141,p1.getLat());
        assertEquals(-75.1308498,p1.getLng());
    }
    @Test
    public void shouldThrowException()
    {
        String s2 ="[{'lat': ' ','lon': ' '}]";
        ResponseEntity<Object> rs2=new ResponseEntity<>(s2,HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(),any())).thenReturn(rs2);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            pointService.findGeoObject(urlNotOK);
        });
    }
    @Test
    public void shouldReturnEmpty()
    {
        String s3 ="[]";
        ResponseEntity<Object> rs3=new ResponseEntity<>(s3,HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(),any())).thenReturn(rs3);
        Point p3=pointService.findGeoObject(urlOK);
        assertNotNull(p3);
    }
}