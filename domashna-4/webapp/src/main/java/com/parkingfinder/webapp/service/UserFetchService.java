package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.UserDto;
import com.parkingfinder.webapp.enumeration.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;


@Service
public class UserFetchService {

    @Autowired
    private RestTemplate restTemplate;

    public Authentication signInUser(HttpServletRequest req, String user, String pass) {
        return restTemplate.exchange("USER-SERVICE/user/sing-in",
                HttpMethod.POST, null, Authentication.class).getBody();
    }

    public UserDto loadUserByUsername(String email) {
        return restTemplate.exchange("USER-SERVICE/user/user-details",
                HttpMethod.POST, new HttpEntity<>(email), UserDto.class).getBody();
    }

    public HttpStatus register(UserDto user) {
        ResponseEntity entity = restTemplate.exchange("USER-SERVICE/user/user-details",
                HttpMethod.POST, new HttpEntity<>(user), ResponseEntity.class).getBody();
        return entity.getStatusCode();
    }

    public HttpStatus updateUser(UserDto user) {
        ResponseEntity entity = restTemplate.exchange("USER-SERVICE/user/user-details",
                HttpMethod.POST, new HttpEntity<>(user), ResponseEntity.class).getBody();
        return entity.getStatusCode();
    }
}
