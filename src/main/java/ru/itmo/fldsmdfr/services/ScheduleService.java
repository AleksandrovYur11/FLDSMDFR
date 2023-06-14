package ru.itmo.fldsmdfr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleService {

    private final SkyDeviceService skyDeviceService;

    @Autowired
    public ScheduleService(SkyDeviceService skyDeviceService) {
        this.skyDeviceService = skyDeviceService;
    }

    @Scheduled(cron = "${breakfastCron}")
    private void breakfastCron() {
        log.info("breakfastCron");
    }

    @Scheduled(cron = "${lunchCron}")
    private void lunchCron() {
        log.info("lunchCron");
    }

    @Scheduled(cron = "${dinnerCron}")
    private void dinnerCron() {
        log.info("dinnerCron");
    }

    @Scheduled(cron = "${deviceCheckCron}")
    private void deviceCheck() {
        skyDeviceService.updateStatusAndLock();
    }

}
