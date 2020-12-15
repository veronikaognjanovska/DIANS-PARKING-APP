package parkingfinder.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super(String.format("User was not found"));
    }
    public UserNotFoundException(String email) {
        super(String.format("User with email: %s was not found", email));
    }

}

