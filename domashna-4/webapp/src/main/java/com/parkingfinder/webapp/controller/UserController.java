package com.parkingfinder.webapp.controller;

import com.parkingfinder.webapp.config.CustomUsernamePasswordAuthenticationProvider;
import com.parkingfinder.webapp.dtos.RouteDto;
import com.parkingfinder.webapp.dtos.User;
import com.parkingfinder.webapp.dtos.UserDto;
import com.parkingfinder.webapp.enumeration.UserManagement;
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

    @Autowired
    private CustomUsernamePasswordAuthenticationProvider authenticationProvider;

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
     * @param email - user email
     * @param pass - user password
     * */
    @PostMapping("/sign-in-post")
    public void login(HttpServletRequest req, String email, String pass) {
        authenticate(req, email, pass);
    }

    private void authenticate(HttpServletRequest request, String email, String pass) {
        SecurityContext sc = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(email, pass);
        Authentication auth = authenticationProvider.authenticate(authReq);
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
    String signUp(@ModelAttribute("user") @Valid UserDto user, BindingResult bindingResult, Model model) {
        return checkBindingResult(user, bindingResult, model,
                "register", "redirect:/sign-in", UserManagement.REGISTER);
    }

    /**
     * Post method for processing user edit request
     * @param user - User data transfer object
     * @param bindingResult - binding result for user object
     * @param model - model object
     * */
    @PostMapping("/update")
    String updateUser(@ModelAttribute("user") @Valid UserDto user, BindingResult bindingResult, Model model)
    {
        return checkBindingResult(user, bindingResult, model,
                "editprofile", "redirect:/user-details", UserManagement.UPDATE);
    }

    private String checkBindingResult(UserDto user, BindingResult bindingResult, Model model, String url, String redirect, UserManagement action) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return url;
        }
        try {
            HttpStatus status = null;
            if (action.equals(UserManagement.UPDATE)){
                status = userService.updateUser(user);
            } else {
                status = userService.register(user);
            }
            return redirect;
        }catch (Exception e) {
            model.addAttribute("user");
            model.addAttribute("emailExists", true);
            return url;
        }
    }

    /**
     * Method that returns edit user form for currently logged in user
     * */
    @GetMapping("/update")
    String updateUp(Model model) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            User user = (User) userService.loadUserByUsername(principal.getName());
            model.addAttribute("user", user);
        }catch (Exception e) {
            return "error/404";
        }
        return "editprofile";
    }
}

