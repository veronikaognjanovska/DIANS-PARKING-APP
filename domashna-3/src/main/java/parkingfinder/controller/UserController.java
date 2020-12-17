

package parkingfinder.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import parkingfinder.config.CustomUsernamePasswordAuthenticationProvider;
import parkingfinder.model.User;
import parkingfinder.model.exception.InvalidArgumentsException;
import parkingfinder.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final CustomUsernamePasswordAuthenticationProvider authenticate ;


    public UserController(UserService userService, CustomUsernamePasswordAuthenticationProvider authenticate) {
        this.userService = userService;
        this.authenticate = authenticate;
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
    String details() {

        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = (User) userService.loadUserByUsername(principal.getEmail());
        return "user-page";
    }

    @GetMapping("/register") // optional error neds to e implemented
    String signUp(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    String signUp(User user) {
        try{
            userService.signUpUser(user);
            return "redirect:/sign-in";
        } catch (InvalidArgumentsException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }

    }







}

