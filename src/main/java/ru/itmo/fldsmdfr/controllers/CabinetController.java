package ru.itmo.fldsmdfr.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DeliveryService;
import ru.itmo.fldsmdfr.services.DishService;
import ru.itmo.fldsmdfr.services.LockService;
import ru.itmo.fldsmdfr.services.VoteService;

import java.util.Optional;

@Controller
public class CabinetController {

    private DishService dishService;
    private VoteService voteService;
    private LockService lockService;
    private DeliveryService deliveryService;

    @Autowired
    public CabinetController(DishService dishService, VoteService voteService, LockService lockService, DeliveryService deliveryService) {
        this.dishService = dishService;
        this.voteService = voteService;
        this.lockService = lockService;
        this.deliveryService = deliveryService;
    }

    @GetMapping("/cabinet")
    public String cabinet(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Optional<? extends GrantedAuthority> authorityOptional = userDetails.getAuthorities().stream().findFirst();
        GrantedAuthority authority = authorityOptional.orElseThrow( () -> new IllegalStateException("user has no role"));
        if (authority.getAuthority().equals(Role.CITIZEN.toString())) {
            model.addAttribute("dishes", dishService.getAllDishes());
            model.addAttribute("voteActive", !lockService.isLocked());
            model.addAttribute("userVoted", voteService.hasUserVotedToday(userDetails.getUser()));
            return "votesDishes";
        } else if (authority.getAuthority().equals(Role.DELIVERYMAN.toString())) {
            model.addAttribute("deliveries", deliveryService.getAllDeliveries());
            return "addressDelivery";
        }
        else if (authority.getAuthority().equals(Role.SCIENTIST.toString())){
            model.addAttribute("isActive", !lockService.isLocked());
            return "maintenance";
        }
        else return "error";
    }

    @ModelAttribute
    private void addAtributes(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("user", userDetails.getUser());
    }
}
