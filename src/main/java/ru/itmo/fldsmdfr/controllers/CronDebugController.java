package ru.itmo.fldsmdfr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.fldsmdfr.services.ScheduleService;
import org.springframework.cloud.context.refresh.ContextRefresher;
import ru.itmo.fldsmdfr.services.VoteService;

import java.util.Map;

@Controller
@Slf4j
public class CronDebugController {

    private final ScheduleService scheduleService;

    private final ContextRefresher contextRefresher;

    private final VoteService voteService;

    @Autowired
    public CronDebugController(ScheduleService scheduleService, ContextRefresher contextRefresher, VoteService voteService) {
        this.scheduleService = scheduleService;
        this.contextRefresher = contextRefresher;
        this.voteService = voteService;
    }

    @GetMapping("/crondebug")
    public String getCronDebugPage(Model model) {
        model.addAttribute("breakfastCron", scheduleService.getBreakfastCron());
        model.addAttribute("lunchCron", scheduleService.getLunchCron());
        model.addAttribute("dinnerCron", scheduleService.getDinnerCron());
        model.addAttribute("deviceCheckCron", scheduleService.getDeviceCheckCron());
        model.addAttribute("voteStartCron", scheduleService.getVoteStartCron());
        model.addAttribute("voteEndCron", scheduleService.getVoteEndCron());
        return "cronDebug";
    }

    @PostMapping("/crondebug")
    public String setCronDebug(@RequestParam Map<String, String> allParams) {
        log.info(allParams.toString());

        scheduleService.setBreakfastCron(allParams.get("breakfastCron"));
        scheduleService.setLunchCron(allParams.get("lunchCron"));
        scheduleService.setDinnerCron(allParams.get("dinnerCron"));
        scheduleService.setDeviceCheckCron(allParams.get("deviceCheckCron"));
        scheduleService.setVoteStartCron(allParams.get("voteStartCron"));
        scheduleService.setVoteEndCron(allParams.get("voteEndCron"));

        contextRefresher.refresh();
        return "redirect:/crondebug";
    }

//    public String
}
