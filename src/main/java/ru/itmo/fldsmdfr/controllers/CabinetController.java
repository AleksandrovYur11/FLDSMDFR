package ru.itmo.fldsmdfr.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.service.RegistrationService;
import ru.itmo.fldsmdfr.util.UserValidator;

import javax.imageio.spi.RegisterableService;
import java.util.Optional;

@Controller
public class CabinetController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public CabinetController(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/cabinet")
    public String cabinet(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<? extends GrantedAuthority> authorityOptional = userDetails.getAuthorities().stream().findFirst();
        if (authorityOptional.isPresent()) {
            GrantedAuthority authority = authorityOptional.get();
            if (authority.getAuthority().equals(Role.CITIZEN.toString())) {
                return "votesDishes";
            } else if (authority.getAuthority().equals(Role.DELIVERYMAN.toString())) {
                return "addressDelivery";
            }
        }
        return "error";
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
    public String perfectRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationService.register(user);
        return "auth/login";

    }

}
