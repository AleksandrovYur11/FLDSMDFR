package ru.itmo.fldsmdfr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;

import java.time.LocalDate;

@Service
@Slf4j
public class ScheduleService {

    private final SkyDeviceService skyDeviceService;

    private final VoteService voteService;

    private final NotificationService notificationService;

    private final DeliveryService deliveryService;

    @Value("${breakfastCron}")
    private String breakfastCron;
    @Value("${lunchCron}")
    private String lunchCron;
    @Value("${dinnerCron}")
    private String dinnerCron;
    @Value("${deviceCheckCron}")
    private String deviceCheckCron;
    @Value("${voteStartCron}")
    private String voteStartCron;
    @Value("${voteEndCron}")
    private String voteEndCron;

    @Autowired
    public ScheduleService(SkyDeviceService skyDeviceService, VoteService voteService, NotificationService notificationService, DeliveryService deliveryService) {
        this.skyDeviceService = skyDeviceService;
        this.voteService = voteService;
        this.notificationService = notificationService;
        this.deliveryService = deliveryService;

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

    @Scheduled(cron = "${voteEndCron}")
    private void voteEndCron() {
        Dish breakfastDish =  voteService.getWinnerDish(LocalDate.now(), FoodTime.BREAKFAST);
        Dish lunchDish =  voteService.getWinnerDish(LocalDate.now(), FoodTime.LUNCH);
        Dish dinnerDish =  voteService.getWinnerDish(LocalDate.now(), FoodTime.DINNER);
        String notification = String.format("Еда, которая выпадет завтра: \n" +
                "на завтрак: %s\n " +
                "на обед: %s\n " +
                "на ужин: %s\n ", breakfastDish.getName(), lunchDish.getName(), dinnerDish.getName());
        notificationService.sendFoodNotificationTelegram(notification);
        notificationService.sendFoodNotificationEmail(notification);
        deliveryService.createDeliveriesForWinners(LocalDate.now());
    }

    public String getBreakfastCron() {
        return breakfastCron;
    }

    public void setBreakfastCron(String breakfastCron) {
        this.breakfastCron = breakfastCron;
    }

    public String getLunchCron() {
        return lunchCron;
    }

    public void setLunchCron(String lunchCron) {
        this.lunchCron = lunchCron;
    }

    public String getDinnerCron() {
        return dinnerCron;
    }

    public void setDinnerCron(String dinnerCron) {
        this.dinnerCron = dinnerCron;
    }

    public String getDeviceCheckCron() {
        return deviceCheckCron;
    }

    public void setDeviceCheckCron(String deviceCheckCron) {
        this.deviceCheckCron = deviceCheckCron;
    }

    public String getVoteStartCron() {
        return voteStartCron;
    }

    public void setVoteStartCron(String voteStartCron) {
        log.info("setting voteStartCron");
        this.voteStartCron = voteStartCron;
        log.info("voteStartCron: {}", getVoteStartCron());
    }

    public String getVoteEndCron() {
        return voteEndCron;
    }

    public void setVoteEndCron(String voteEndCron) {
        this.voteEndCron = voteEndCron;
    }
}
