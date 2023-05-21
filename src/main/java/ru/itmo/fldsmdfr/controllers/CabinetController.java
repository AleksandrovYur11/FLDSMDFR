package ru.itmo.fldsmdfr.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {
    @GetMapping("/votesDishes")
    public String votesDish() {
        return "votesDishes";
    }

    @GetMapping("/addressDelivery")
    public String addressDelivery() {
        return "addressDelivery";
    }
}
