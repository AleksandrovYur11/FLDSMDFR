package ru.itmo.fldsmdfr.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.security.services.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DeliveryService;
import ru.itmo.fldsmdfr.services.DishService;
import ru.itmo.fldsmdfr.services.LockService;
import ru.itmo.fldsmdfr.services.VoteService;

import java.util.Optional;

@RestController
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
    public ModelAndView cabinet(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelAndView modelAndView) {
        Optional<? extends GrantedAuthority> authorityOptional = userDetails.getAuthorities().stream().findFirst();
        GrantedAuthority authority = authorityOptional.orElseThrow(() -> new IllegalStateException("user has no role"));
        if (authority.getAuthority().equals(Role.CITIZEN.toString())) {
            modelAndView.addObject("dishes", dishService.getAllDishes());
            modelAndView.addObject("voteActive", !lockService.isLocked());
            modelAndView.addObject("userVoted", voteService.hasUserVotedToday(userDetails.getUser()));
            modelAndView.addObject("voteInProgress", voteService.isVoteActive());
            modelAndView.setViewName("votesDishes");
        } else if (authority.getAuthority().equals(Role.DELIVERYMAN.toString())) {
            modelAndView.addObject("deliveries", deliveryService.getAllDeliveries());
            modelAndView.setViewName("addressDelivery");
        } else if (authority.getAuthority().equals(Role.SCIENTIST.toString())) {
            modelAndView.addObject("isActive", !lockService.isLocked());
            modelAndView.setViewName("maintenance");
        } else {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @ModelAttribute
    private void addAtributes(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("user", userDetails.getUser());
    }
}
