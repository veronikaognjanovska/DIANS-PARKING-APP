package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Service for communicating with external user microservice
 * */
@Service
public class UserFetchService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Performs a sing in on the current user
     * @param email - string that represents the user's email
     * @param pass - string that represents the user's password
     * @return Authentication - authentication object
     * */
    public Authentication signInUser(String email, String pass) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(pass);
        return restTemplate.exchange("http://USER-SERVICE/user/sing-in",
                HttpMethod.POST, new HttpEntity<>(userDto), Authentication.class).getBody();
    }

    /**
     * Fetches the currently logged in user's details
     * @param email - string that represents the user's email
     * @return UserDto - user data transfer object
     * */
    public UserDto loadUserByUsername(String email) {
        return restTemplate.exchange("http://USER-SERVICE/user/user-details",
                HttpMethod.POST, new HttpEntity<>(email), UserDto.class).getBody();
    }

    /**
     * Performs user registration
     * @param user - user data transfer object
     * @return HttpStatus - the received http status code
     * */
    public HttpStatus register(UserDto user) {
        ResponseEntity entity = restTemplate.exchange("http://USER-SERVICE/user/user-details",
                HttpMethod.POST, new HttpEntity<>(user), ResponseEntity.class).getBody();
        return entity.getStatusCode();
    }

    /**
     * Performs update of user data
     * @param user - user data transfer object
     * @return HttpStatus - the received http status code
     * */
    public HttpStatus updateUser(UserDto user) {
        ResponseEntity entity = restTemplate.exchange("http://USER-SERVICE/user/user-details",
                HttpMethod.POST, new HttpEntity<>(user), ResponseEntity.class).getBody();
        return entity.getStatusCode();
    }
}
