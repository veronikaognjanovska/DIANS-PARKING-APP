

package com.parkingfinder.webapp.controller;



import com.parkingfinder.webapp.dtos.RouteDto;
import com.parkingfinder.webapp.dtos.UserDto;
import com.parkingfinder.webapp.exception.InvalidArgumentsException;
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

@Controller
public class UserController {

    @Autowired
    private UserFetchService userService;

    @Autowired
    private RouteFetchService routeService;

    private final Comparator<RouteDto> routeComparator = Comparator.comparing(RouteDto::getTimestamp).reversed();

    @GetMapping("/sign-in")
    String signIn() {
        return "sign-in";
    }

    @GetMapping("/sign-in-error")
    String signInError(Model model) {
        model.addAttribute("loginError", true);
        return "sign-in";
    }

    @PostMapping("/sign-in-post")
    public void login(HttpServletRequest req, String user, String pass) {
        Authentication auth = userService.signInUser(req, user, pass);
        if (auth!=null) {
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = req.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        }
    }

    @GetMapping("/user-details")
    String details(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDto user = userService.loadUserByUsername(email);
        if (user!=null) {
            List<RouteDto> routes = routeService.findHistoryRoutes(email)
                    .stream()
                    .limit(5)
                    .sorted(routeComparator)
                    .collect(Collectors.toList());
            model.addAttribute("routes", routes);
            model.addAttribute("user", user);
            return "user-page";
        }
        return "/error/404";
    }

    @GetMapping("/register")
    String signUp(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

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

    @PostMapping("/update")
    String updateUser(@Valid UserDto user, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors()) {
            return "editprofile";
        }

        try{
            userService.updateUser(user);
            return "redirect:/user-details";
        } catch (InvalidArgumentsException exception) {
            model.addAttribute("emailExists", true);
            return "editprofile";
        }

    }

    @GetMapping("/update")
    String updateUp(Model model) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto user = (UserDto) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "editprofile";
    }

}

