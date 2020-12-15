package parkingfinder.service;

import org.springframework.stereotype.Service;
import parkingfinder.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
