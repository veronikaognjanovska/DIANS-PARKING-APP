

package parkingfinder.service;



import com.sun.imageio.plugins.common.SingleTileRenderedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import parkingfinder.enumeration.UserRole;
import parkingfinder.model.User;
import parkingfinder.model.exception.InvalidArgumentsException;
import parkingfinder.repository.UserRepository;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


   // public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
     //   this.passwordEncoder = passwordEncoder;
       // this.userRepository=userRepository;
    //}


    public boolean updateUser(User user) throws InvalidArgumentsException
    {
            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            if(!this.userRepository.findByEmail(email).isPresent()) {
            throw new InvalidArgumentsException();
        }
        userRepository.updateUserById(user.getName(),user.getEmail(),passwordEncoder.encode(user.getPassword()));


        return true;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userRepository.findByEmail(email);

        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));

    }

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

