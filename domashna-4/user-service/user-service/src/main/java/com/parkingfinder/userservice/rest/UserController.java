package com.parkingfinder.userservice.rest;

import com.parkingfinder.userservice.model.User;
import com.parkingfinder.userservice.model.exception.InvalidArgumentsException;
import com.parkingfinder.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest controller for user management
 * @author Anastasija Petrovska
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Method that finds the user and returns user's details
     * @param email - string that represents the user's email
     * @return user - an object of the found user
     */
    @PostMapping("/user-details")
    User details(@RequestBody String email) {

        User user = (User) userService.loadUserByUsername(email);
        return user;
    }

    /**
     * Method that enables an user to sign up
     * @param user - object of the user
     * @param bindingResult - the result
     * @return HttpStatus - the HttpStatus depends if the binding result has errors, if it is a success or
     * if there is an exception and corresponds accordingly with the different statuses: Not_Acceptable, OK, Conflict
     */
    @PostMapping("/register")
    ResponseEntity signUp(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        try{
            userService.signUpUser(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (InvalidArgumentsException exception) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    /**
     * Method that enables updating of the user's details
     * @param user - object that represents the user
     * @param bindingResult - the result
     * @param model - the model of the user
     * @return HttpStatus - the HttpStatus depends if the binding result has errors, if it is a success or
     * if there is an exception and corresponds accordingly with the different statuses: Not_Acceptable, OK, Conflict
     */
    @PostMapping("/update")
    ResponseEntity updateUser(
            @RequestBody @Valid User user, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try{
            userService.updateUser(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (InvalidArgumentsException exception) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

    }
}