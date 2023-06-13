package ru.itmo.fldsmdfr.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.service.RegistrationService;

@Controller
public class LoginController {

    private final RegistrationService registrationService;

    @Autowired
    public LoginController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user) {
        return "/auth/registration";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/auth/login";
    }

    @PostMapping("/registration")
    public String perfectRegistration(@ModelAttribute("user") @Valid User user) {
        registrationService.register(user);
        return "/auth/login";
    }

//    @GetMapping("/index")
//    public String getPage(){
//        return "login";
//    }
}
