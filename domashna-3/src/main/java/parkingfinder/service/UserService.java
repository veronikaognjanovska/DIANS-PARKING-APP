

package parkingfinder.service;



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

    private final UserRepository userRepository;

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userRepository.findByEmail(email);

        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));

    }

//    public void signUpUser(User user, UserRole role) {
//
//        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
//
//        user.setPassword(encryptedPassword);
//
//        final User createdUser = userRepository.save(user);
//
//    }
    public void signUpUser(String name,String surname,String email,String password) throws InvalidArgumentsException{

        if (name==null || name.isEmpty()  || surname==null || surname.isEmpty()  ||
                password==null || password.isEmpty() || email==null || email.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        if(this.userRepository.findByEmail(email).isPresent()) {
            throw new InvalidArgumentsException();
        }
        User user = new User(String.format("%s %s",name,surname),email,
                passwordEncoder.encode(password),UserRole.USER);
        userRepository.save(user);
    }


}

