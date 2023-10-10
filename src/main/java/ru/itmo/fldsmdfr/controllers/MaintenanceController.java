package ru.itmo.fldsmdfr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.fldsmdfr.dto.LockStatusDto;
import ru.itmo.fldsmdfr.models.LockStatus;
import ru.itmo.fldsmdfr.services.LockService;

@Controller
@Slf4j
public class MaintenanceController {

    private LockService lockService;

    @Autowired
    public MaintenanceController(LockService lockService) {
        this.lockService = lockService;
    }

    @PostMapping("/maintain")
    public String maintain() {
        if(lockService.isLocked()) {
            lockService.saveLock(LockStatusDto.builder().lockStatus(LockStatus.UNLOCK).build());
            log.info("system unlocked by scientist");
        }
        else {
            log.info("nothing to unlock by scientist");
        }
        return "redirect:/cabinet";
    }
}
