package ru.itmo.fldsmdfrmock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itmo.fldsmdfrmock.services.StatusService;

@Controller
public class StatusController {

    private StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping(value = "/status", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getStatus() {
        return statusService.getStatus();
    }

    @GetMapping(value = "/")
    public String getAdminUi(Model model) {
        model.addAttribute("status", statusService.getStatus());
        return "admin-ui";
    }

    @PostMapping("/setok")
    public String setOk() {
        statusService.setStatus("ok");
        return "redirect:/";
    }

    @PostMapping("/setbroken")
    public String setBroken() {
        statusService.setStatus("broken");
        return "redirect:/";
    }
}
