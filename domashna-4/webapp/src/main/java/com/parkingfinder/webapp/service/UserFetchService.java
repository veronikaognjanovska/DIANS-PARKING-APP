package com.parkingfinder.webapp.service;

import com.parkingfinder.webapp.dtos.User;
import com.parkingfinder.webapp.dtos.UserDto;
import com.parkingfinder.webapp.util.URLPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Service for communicating with external user microservice
 * */
@Service
public class UserFetchService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetches the currently logged in user's details
     * @param email - string that represents the user's email
     * @return UserDto - user data transfer object
     * */
    public UserDetails loadUserByUsername(String email) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(URLPaths.USER_SERVICE_BASE_URL
                        + URLPaths.USER_DETAILS,
                HttpMethod.POST, new HttpEntity<>(email, httpHeaders), User.class).getBody();
    }

    /**
     * Performs user registration
     * @param user - user data transfer object
     * @return HttpStatus - the received http status code
     * */
    public HttpStatus register(UserDto user) {
        return getHttpStatusCodeFromRequest(URLPaths.USER_SERVICE_BASE_URL
                + URLPaths.USER_REGISTER, user);
    }

    /**
     * Performs update of user data
     * @param user - user data transfer object
     * @return HttpStatus - the received http status code
     * */
    public HttpStatus updateUser(UserDto user) {
        return getHttpStatusCodeFromRequest(URLPaths.USER_SERVICE_BASE_URL
                + URLPaths.USER_UPDATE, user);
    }

    private HttpStatus getHttpStatusCodeFromRequest(String url, UserDto user) {
        return restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(user), ResponseEntity.class).getStatusCode();
    }
}
