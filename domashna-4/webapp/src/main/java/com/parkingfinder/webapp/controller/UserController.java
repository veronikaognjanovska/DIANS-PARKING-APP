package com.parkingfinder.webapp.controller;

import com.parkingfinder.webapp.dtos.RouteDto;
import com.parkingfinder.webapp.dtos.UserDto;
import com.parkingfinder.webapp.service.RouteFetchService;
import com.parkingfinder.webapp.service.UserFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Controller that returns html views for user pages
 * \n Uses the UserFetchService and RouteFetchService
 * */
@Controller
public class UserController {

    @Autowired
    private UserFetchService userService;

    @Autowired
    private RouteFetchService routeService;

    private final Comparator<RouteDto> routeComparator = Comparator.comparing(RouteDto::getTimestamp).reversed();

    /**
    * Returns simple sign in form
    * */
    @GetMapping("/sign-in")
    String signIn() {
        return "sign-in";
    }

    /**
    * Handler for user sing in error
    * @param model - Model object used to set error attribute
    * */
    @GetMapping("/sign-in-error")
    String signInError(Model model) {
        model.addAttribute("loginError", true);
        return "sign-in";
    }

    /**
     * Successful sing in handler method
     * @param req - current request of type HttpServletRequest
     * @param user - user email
     * @param pass - user password
     * */
    @PostMapping("/sign-in-post")
    public void login(HttpServletRequest req, String user, String pass) {
        Authentication auth = userService.signInUser(user, pass);
        if (auth!=null) {
            authenticate(req, auth);
        }
    }

    private void authenticate(HttpServletRequest request, Authentication authentication) {
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    /**
     * Method that retrieves user details for currently logged in user
     * @param model - Model object for setting user and routes
     * */
    @GetMapping("/user-details")
    String details(Model model) {
        UserDto user = fetchCurrentlyLoggedInUser();
        if (user!=null) {
            model.addAttribute("routes", findRoutesForUser(user.getEmail()));
            model.addAttribute("user", user);
            return "user-page";
        }
        return "/error/404";
    }

    private UserDto fetchCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.loadUserByUsername(email);
    }

    private List<RouteDto> findRoutesForUser(String email) {
        return routeService.findHistoryRoutes(email)
                .stream()
                .limit(5)
                .sorted(routeComparator)
                .collect(Collectors.toList());
    }

    /**
    * Method that returns simple user register form
    * */
    @GetMapping("/register")
    String signUp(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    /**
    * Post method that handles user registration
     * @param user - User data transfer object
     * @param bindingResult - binding result for user object
     * @param model - model object
    * */
    @PostMapping("/register")
    String signUp(@ModelAttribute @Valid UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        HttpStatus status = userService.register(user);
        if(status.equals(HttpStatus.OK)){
               return "redirect:/sign-in";
        }
        else if (status.equals(HttpStatus.CONFLICT)){
            model.addAttribute("emailExists", true);
            return "register";
        }
        else {
            model.addAttribute("invalidArgs", true);
            return "register";
        }
    }

    /**
     * Post method for processing user edit request
     * @param user - User data transfer object
     * @param bindingResult - binding result for user object
     * @param model - model object
     * */
    @PostMapping("/update")
    String updateUser(@Valid UserDto user, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors()) {
            return "editprofile";
        }
        HttpStatus status = userService.updateUser(user);
        if (status.equals(HttpStatus.OK)){
            return "redirect:/user-details";
        } else if (status.equals(HttpStatus.CONFLICT)){
            model.addAttribute("emailExists", true);
            return "editprofile";
        }
        else {
            model.addAttribute("invalidArgs", true);
            return "editprofile";
        }
    }

    /**
     * Method that returns edit user form for currently logged in user
     * */
    @GetMapping("/update")
    String updateUp(Model model) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto user = (UserDto) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "editprofile";
    }
}

