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

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;

    @PostMapping("/sign-in")
    public Authentication login(HttpServletRequest req, String user, String pass) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user, pass);
        Authentication auth = customUsernamePasswordAuthenticationProvider.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return auth;
    }

    @PostMapping("/user-details")
    User details(@RequestBody String email) {

        User user = (User) userService.loadUserByUsername(email);
        return user;
    }

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