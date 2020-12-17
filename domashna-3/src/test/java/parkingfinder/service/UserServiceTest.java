package parkingfinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import parkingfinder.model.User;
import parkingfinder.model.exception.InvalidArgumentsException;
import parkingfinder.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void init(){
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new User()));

        when(passwordEncoder.encode(any())).thenReturn("proba");

        when(userRepository.save(any())).thenReturn(new User());
    }


    @Test
    public void shouldLoadUserByUsername(){

        //slucaj 1 - pozitiven
        UserDetails user = userService.loadUserByUsername("anastasija");
        assertNotNull(user);


        //slucaj 2 - negativen
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        try {
            user = userService.loadUserByUsername("kgldsmf");
        }catch (UsernameNotFoundException exception){
            assertEquals("User with email kgldsmf cannot be found.", exception.getMessage());
        }

    }


    @Test
    public void shouldSignUpUser(){

        //slucaj 1 - pozitiven
        User user = new User();
        user.setEmail("user@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Boolean b = userService.signUpUser(user);
        assertTrue(b);



        //slucaj 2 - negativen
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            userService.signUpUser(user);
        });

    }

}