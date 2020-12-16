

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
import parkingfinder.model.exception.InvalidArgumentsException;
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

//    @PostMapping("/register")
//    String signUp(User user) {
//
//        userService.signUpUser(user);
//
//        return "redirect:/sign-in";
//    }

    @PostMapping("/register")
    String signUp(@RequestParam String name,
                  @RequestParam String surname,
                  @RequestParam String email,
                  @RequestParam String password
    ) {
        try{
            userService.signUpUser(name,surname,email,password);
            return "redirect:/sign-in";
        } catch (InvalidArgumentsException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }

    }



}

