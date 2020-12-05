package parkingapp.app.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import parkingapp.app.model.ConfirmationToken;
import parkingapp.app.model.User;
import parkingapp.app.repository.ConfirmationTokenRepository;
import parkingapp.app.repository.UserRepository;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }
    }

    void signUpUser(User user) {

        final String encryptedPassword = encoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        final User createdUser = userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);

        Object confirmationTokenService;
        confirmationTokenRepository.saveConfirmationToken(confirmationToken);



    }

    void deleteConfirmationToken(Long id){

        confirmationTokenRepository.deleteById(id);
    }


}