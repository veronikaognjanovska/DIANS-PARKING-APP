package com.parkingfinder.webapp.config;

import com.parkingfinder.webapp.dtos.User;
import com.parkingfinder.webapp.exception.UserNotFoundException;
import com.parkingfinder.webapp.service.UserFetchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component that provides username and password authentication
 * Implements Authentication Provider interface
 */
@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserFetchService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user = new User();

    private void areCredentialsInvalid(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Invalid Credentials");
        }
    }

    private void isPasswordIncorrect(String password, UserDetails userDetails){
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Password is incorrect!");
        }
    }


    /**
     * Method that does authentication
     * @param authentication - object of class Authentication
     * @return token - new token for the Username and Password authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        areCredentialsInvalid(email,password);

        UserDetails userDetails = this.userService.loadUserByUsername(email);
        if (userDetails == null) {
            return null;
        }
        isPasswordIncorrect(password, userDetails);

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    }

    public void setUser(User user) {
        this.user = user;
    }
    /**
     * Method that examines if the class given as parameter equals the token class
     * @param aClass - class that is examined if it is supported
     * @return - boolean if the token class supports the aClass given as parameter
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}