package com.parkingfinder.webapp.controller;

import com.parkingfinder.webapp.config.CustomUsernamePasswordAuthenticationProvider;
import com.parkingfinder.webapp.dtos.RouteDto;
import com.parkingfinder.webapp.dtos.User;
import com.parkingfinder.webapp.dtos.UserDto;
import com.parkingfinder.webapp.enumeration.UserManagement;
import com.parkingfinder.webapp.exception.UserNotFoundException;
import com.parkingfinder.webapp.rest.AuthenticationController;
import com.parkingfinder.webapp.service.RouteFetchService;
import com.parkingfinder.webapp.service.UserFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Controller that returns html views for user pages
 * \n Uses the UserFetchService and RouteFetchService
 * */
@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserFetchService userService;

    @Autowired
    private RouteFetchService routeService;

    @Autowired
    private CustomUsernamePasswordAuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationController authenticationController;

    private final Comparator<RouteDto> routeComparator = Comparator.comparing(RouteDto::getTimestamp).reversed();

    /**
    * Returns simple sign in form
    * */
    @GetMapping("/login")
    String signIn() {
        authenticationController.setCurrentUser(null);
        return "sign-in";
    }

    /**
    * Handler for user sing in error
    * @param model - Model object used to set error attribute
    * */
    @GetMapping("/login-error")
    String signInError(Model model) {
        model.addAttribute("loginError", true);
        return "sign-in";
    }

    /**
     * Successful sing in handler method
     * @param req - current request of type HttpServletRequest
     * @param username - user email
     * @param password - user password
     * */
    @PostMapping("/login")
    public String login(HttpServletRequest req, String username, String password) {
        try {
            authenticate(req, username, password);
            return "home";
        }catch (Exception e){
            return "redirect:/login-error";
        }
    }

    private void authenticate(HttpServletRequest request, String email, String pass) {
        SecurityContext sc = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(email, pass);
        Authentication auth = authenticationProvider.authenticate(authReq);
        System.out.println("Auth: " + auth.getName());
        if (auth==null) {
            throw new UserNotFoundException();
        }
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    /**
     * Method that retrieves user details for currently logged in user
     * @param model - Model object for setting user and routes
     * */
    @GetMapping("/user-details")
    String details(Model model) {
        try{
        User user = fetchCurrentlyLoggedInUser();
            if (user!=null) {
                model.addAttribute("routes", findRoutesForUser(user.getEmail()));
                model.addAttribute("user", user);
                return "user-page";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/error/404";
    }

    private User fetchCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return (User) userService.loadUserByUsername(email);
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
        model.addAttribute("user", new User());
        return "register";
    }

    /**
    * Post method that handles user registration
     * @param user - User data transfer object
     * @param bindingResult - binding result for user object
     * @param model - model object
    * */
    @PostMapping("/register")
    String signUp(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        return checkBindingResult(user, bindingResult, model,
                "register", "redirect:/login", UserManagement.REGISTER);
    }

    /**
     * Post method for processing user edit request
     * @param user - User data transfer object
     * @param bindingResult - binding result for user object
     * @param model - model object
     * */
    @PostMapping("/update")
    String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model)
    {
        return checkBindingResult(user, bindingResult, model,
                "editprofile", "redirect:/user-details", UserManagement.UPDATE);
    }

    private String checkBindingResult(User user, BindingResult bindingResult, Model model, String url, String redirect, UserManagement action) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return url;
        }
        try {
            HttpStatus status = null;
            UserDto userDto = mapUserToUserDto(user);
            if (action.equals(UserManagement.UPDATE)){
                status = userService.updateUser(userDto);
            } else {
                status = userService.register(userDto);
            }
            return redirect;
        }catch (Exception e) {
            model.addAttribute("user");
            model.addAttribute("emailExists", true);
            return url;
        }
    }

    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setID(user.getID());
        return userDto;
    }

    /**
     * Method that returns edit user form for currently logged in user
     * @param model - Model object for sending attributes to html page
     * */
    @GetMapping("/update")
    String updateUp(Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            User user = (User) userService.loadUserByUsername(principal.getEmail());
            model.addAttribute("user", user);
        }catch (Exception e) {
            return "error/404";
        }
        return "editprofile";
    }
}

