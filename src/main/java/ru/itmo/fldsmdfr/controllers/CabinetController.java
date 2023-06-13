package ru.itmo.fldsmdfr.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DishService;

import java.util.Optional;

@Controller
public class CabinetController {

    private DishService dishService;

    @Autowired
    public CabinetController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/cabinet")
    public String cabinet(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Optional<? extends GrantedAuthority> authorityOptional = userDetails.getAuthorities().stream().findFirst();
        GrantedAuthority authority = authorityOptional.orElseThrow( () -> new IllegalStateException("user has no role"));
        if (authority.getAuthority().equals(Role.CITIZEN.toString())) {
            model.addAttribute("dishes", dishService.getAllDishes());
            return "votesDishes";
        } else if (authority.getAuthority().equals(Role.DELIVERYMAN.toString())) {
            return "addressDelivery";
        }
        else {
            return "maintanence";
        }
    }
}
