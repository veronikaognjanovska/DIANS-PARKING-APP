package com.parkingfinder.userservice.rest;

import com.parkingfinder.userservice.config.CustomUsernamePasswordAuthenticationProvider;
import com.parkingfinder.userservice.model.User;
import com.parkingfinder.userservice.model.exception.InvalidArgumentsException;
import com.parkingfinder.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Rest controller for user management
 * @author Anastasija Petrovska
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;

    private void setAuthAttribute(Authentication auth, HttpServletRequest req){
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    /**
     * Method that enables the login of users with authentication
     * @param req - a Http Servlet request
     * @param user - string that represents the user
     * @param pass - string that represents the user's password
     * @return auth - returns if the user is authenticated and logged in correctly
     */
    @PostMapping("/sign-in")
    public Authentication login(HttpServletRequest req, String user, String pass) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user, pass);
        Authentication auth = customUsernamePasswordAuthenticationProvider.authenticate(authReq);

        setAuthAttribute(auth,req);

        return auth;
    }

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
     * @param model - model of the user
     * @return HttpStatus - the HttpStatus depends if the binding result has errors, if it is a success or
     * if there is an exception and corresponds accordingly with the different statuses: Not_Acceptable, OK, Conflict
     */
    @PostMapping("/register")
    ResponseEntity signUp(@RequestBody @Valid User user, BindingResult bindingResult, Model model) {
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