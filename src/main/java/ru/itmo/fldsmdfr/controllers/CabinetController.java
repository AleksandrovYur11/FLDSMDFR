package ru.itmo.fldsmdfr.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.service.RegistrationService;

import java.util.Optional;

@Controller
public class CabinetController {


    private final RegistrationService registrationService;

    @Autowired
    public CabinetController( RegistrationService registrationService) {
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


}
