package ru.itmo.fldsmdfr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.fldsmdfr.services.ScheduleService;

import java.util.Map;

@Controller
@Slf4j
public class CronDebugController {

    private final ScheduleService scheduleService;

    @Autowired
    public CronDebugController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/crondebug")
    public String getCronDebugPage() {
        return "cronDebug";
    }

    @PostMapping("/crondebug")
    public String setCronDebug(@RequestParam Map<String, String> allParams) {
        log.info(allParams.toString());
        return "redirect:/crondebug";
    }
}
