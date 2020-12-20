

package parkingfinder.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import parkingfinder.config.CustomUsernamePasswordAuthenticationProvider;
import parkingfinder.model.Route;
import parkingfinder.model.User;
import parkingfinder.model.exception.InvalidArgumentsException;
import parkingfinder.service.RouteService;
import parkingfinder.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final CustomUsernamePasswordAuthenticationProvider authenticate ;

    @Autowired
    private final RouteService routeService;

    private final Comparator<Route> routeComparator = Comparator.comparing(Route::getTimestamp).reversed();


    public UserController(UserService userService, CustomUsernamePasswordAuthenticationProvider authenticate, RouteService routeService) {
        this.userService = userService;
        this.authenticate = authenticate;
        this.routeService = routeService;
    }


    @GetMapping("/sign-in")// optional error neds to e implemented
    String signIn(HttpServletRequest request, Model model) {
        return "sign-in";
    }

    @GetMapping("/sign-in-error")// optional error neds to e implemented
    String signInError(Model model) {
        model.addAttribute("loginError", true);
        return "sign-in";
    }

    @PostMapping("/sign-in-post")
    public void login(HttpServletRequest req, String user, String pass) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user, pass);
        Authentication auth = authenticate.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    @GetMapping("/user-details")
    String details(Model model) {

       // User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       // User user = (User) userService.loadUserByUsername(principal.getEmail());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = (User) userService.loadUserByUsername(email);
        List<Route> routes = routeService.findHistoryRoutes(email)
                .stream()
                .limit(5)
                .sorted(routeComparator)
                .collect(Collectors.toList());
        model.addAttribute("routes", routes);
        model.addAttribute("user", user);

        return "user-page";
    }

    @GetMapping("/register") // optional error neds to e implemented
    String signUp(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    String signUp(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try{
            userService.signUpUser(user);
            return "redirect:/sign-in";
        } catch (InvalidArgumentsException exception) {
            model.addAttribute("emailExists", true);
            return "register";
        }
    }

    @PostMapping("/update")
    String updateUser(
            @Valid User user, BindingResult bindingResult, Model model)
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
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) userService.loadUserByUsername(principal.getEmail());
        model.addAttribute("user", user);
        return "editprofile";
    }


}

