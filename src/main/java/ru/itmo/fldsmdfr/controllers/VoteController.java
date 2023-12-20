package ru.itmo.fldsmdfr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.fldsmdfr.dto.UserVoteDto;
import ru.itmo.fldsmdfr.security.services.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DishService;
import ru.itmo.fldsmdfr.services.VoteService;

import java.util.Map;

@RestController
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
        if(!allParams.containsKey("breakfast") || !allParams.containsKey("lunch") || !allParams.containsKey("dinner")) {
            throw new IllegalArgumentException("All choices must be provided: breakfast, lunch, dinner");
        }
       log.info("saving vote {} for user {}", allParams, userDetails);
        UserVoteDto userVoteDto = UserVoteDto.builder()
                    .user(userDetails.getUser())
                    .breakfastDishId(Long.parseLong(allParams.get("breakfast")))
                    .lunchDishId(Long.parseLong(allParams.get("lunch")))
                    .dinnerDishId(Long.parseLong(allParams.get("dinner")))
                    .build();
       voteService.saveVote(userVoteDto);
       return "redirect:/cabinet";
    }
}
