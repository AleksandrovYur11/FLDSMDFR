package ru.itmo.fldsmdfr.controllers;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DishService;
import ru.itmo.fldsmdfr.services.VoteService;

import java.util.Map;

@Controller
@Slf4j
public class VoteController {

    private VoteService voteService;
    private DishService dishService;

    @Autowired
    public VoteController(VoteService voteService, DishService dishService) {
        this.voteService = voteService;
        this.dishService = dishService;
    }

    @PostMapping("/vote")
    public String vote(@RequestParam Map<String, String> allParams, @AuthenticationPrincipal UserDetailsImpl userDetails) {
       log.info("saving vote {} for user {}", allParams, userDetails);
       voteService.saveVote(allParams, userDetails);
       return "redirect:/cabinet";
    }
}
