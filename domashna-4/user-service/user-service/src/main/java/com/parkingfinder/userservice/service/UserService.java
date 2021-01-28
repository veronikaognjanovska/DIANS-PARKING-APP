package com.parkingfinder.userservice.service;


import com.parkingfinder.userservice.model.exception.InvalidArgumentsException;
import com.parkingfinder.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.parkingfinder.userservice.enumeration.UserRole;
import com.parkingfinder.userservice.model.User;


import java.text.MessageFormat;
import java.util.Optional;

/**
 * Service for User details manipulation
 * Implements the UserDetailsService interface
 * @author Anastasija Petrovska
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void setUser(User user){
        User update = this.userRepository.findById(user.getID()).get();
        update.setEmail(user.getEmail());
        update.setPassword(passwordEncoder.encode(user.getPassword()));
        update.setName(user.getName());
        userRepository.save(update);
    }

    /**
     * Method that updates the user
     * @param user - object that represents the user
     * @return true - if the user is successfully updated
     * @throws InvalidArgumentsException - throws the exception if the details of the user are not valid
     */
    public boolean updateUser(User user) throws InvalidArgumentsException
    {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User u = this.userRepository.findByEmail(email).get();
        if(u!=null && !u.getID().equals(user.getID())) {
            throw new InvalidArgumentsException();
        }
        try {
            setUser(user);
        }catch (Exception e){
            throw new InvalidArgumentsException();
        }
        return true;
    }

    /**
     * Method that loads the user by its username
     * @param email - string that represents the email of the user which is also it's username
     * @return user - optional, returns a user only if the user is loaded and found
     * @throws UsernameNotFoundException - throws the exception if the user is not found by the username
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userRepository.findByEmail(email);

        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));

    }

    /**
     * Method that signs up the new user
     * @param user - object that represents the user
     * @return true - if the sign up has been made successfully
     * @throws InvalidArgumentsException - throws the exception if the details of the user are not valid
     */
    public boolean signUpUser(User user) throws InvalidArgumentsException{

        if(this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new InvalidArgumentsException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);
        userRepository.save(user);

        return true;

    }


}
