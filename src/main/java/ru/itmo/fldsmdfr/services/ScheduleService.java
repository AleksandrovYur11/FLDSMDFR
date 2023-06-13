package ru.itmo.fldsmdfr.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ScheduleService {

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

}
