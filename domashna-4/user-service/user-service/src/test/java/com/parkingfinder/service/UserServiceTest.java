package com.parkingfinder.service;

import com.parkingfinder.userservice.model.User;
import com.parkingfinder.userservice.model.exception.InvalidArgumentsException;
import com.parkingfinder.userservice.repository.UserRepository;
import com.parkingfinder.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private void saveUser(User user){
        when(passwordEncoder.encode(any())).thenReturn("proba");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(new User());
    }

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void init(){
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new User()));
    }


    @Test
    public void shouldLoadUserByUsername(){

        UserDetails user = userService.loadUserByUsername("anastasija");
        assertNotNull(user);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        try {
            user = userService.loadUserByUsername("kgldsmf");
        }catch (UsernameNotFoundException exception){
            assertEquals("User with email kgldsmf cannot be found.", exception.getMessage());
        }
    }


    @Test
    public void shouldSignUpUser(){

        User user = new User();
        user.setEmail("user@gmail.com");

        saveUser(user);

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            userService.signUpUser(user);
        });


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        Boolean b = userService.signUpUser(user);
        assertTrue(b);

    }

    private void setUp(){
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public Object getCredentials() {
                        return null;
                    }

                    @Override
                    public Object getDetails() {
                        return null;
                    }

                    @Override
                    public Object getPrincipal() {
                        return null;
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return false;
                    }

                    @Override
                    public void setAuthenticated(boolean b) throws IllegalArgumentException {

                    }

                    @Override
                    public String getName() {
                        return "email";
                    }
                };
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        });
    }

    @Test
    public void shouldUpdateUser() {

        setUp();

        User user = new User();
        user.setID(1);
        user.setEmail("email");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        boolean result  = userService.updateUser(user);
        assertTrue(result);

        User user2 = new User();
        user2.setID(2);
        user2.setEmail("email");
        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            userService.signUpUser(user2);
        });
    }
}