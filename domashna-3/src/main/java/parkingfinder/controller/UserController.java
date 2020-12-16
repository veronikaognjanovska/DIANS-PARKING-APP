package parkingfinder.controller;



import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import parkingfinder.model.User;
import parkingfinder.service.UserService;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;



    @GetMapping("/sign-in")
    String signIn() {

        return "sign-in";
    }

    @GetMapping("/user-details")
    String details(Model model) {

        //User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //User user = (User) userService.loadUserByUsername(principal.getEmail());

        //model.addAttribute("user",user);

        return "user-page";
    }

    @GetMapping("/register")
    String signUp(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    String signUp(User user) {

        userService.signUpUser(user);

        return "redirect:/sign-in";
    }



}