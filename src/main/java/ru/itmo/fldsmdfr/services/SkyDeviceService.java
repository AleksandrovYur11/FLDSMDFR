package ru.itmo.fldsmdfr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itmo.fldsmdfr.dto.LockStatusDto;
import ru.itmo.fldsmdfr.models.LockStatus;

@Service
@Slf4j
public class SkyDeviceService {

    private final String skyDeviceStatusUrl;

    private final LockService lockService;

    private final NotificationService notificationService;


    @Autowired
    public SkyDeviceService(@Value("${skydeviceStatusUrl}")String skyDeviceUrl, LockService lockService, NotificationService notificationService) {
        this.skyDeviceStatusUrl = skyDeviceUrl;
        this.lockService = lockService;
        this.notificationService = notificationService;
    }

    public void updateStatusAndLock() {
        log.info("updating sky device status");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(skyDeviceStatusUrl, String.class);
            if ("ok".equalsIgnoreCase(response)) {
                if (lockService.isLocked()) {
                    log.info("sky device is ok now - unlocking");
                    lockService.saveLock(LockStatusDto.builder().lockStatus(LockStatus.UNLOCK).build());
                }
            } else {
                processBadState(response);
            }
        } catch (Exception e) {
            log.error("couldn't get sky device status: ", e);
            processBadState(e.getMessage());
        }
    }


    private void processBadState(String response) {
        if (!lockService.isLocked()) {
            log.warn("sky device is broken (responded with {}) - locking", response);
            lockService.saveLock(LockStatusDto.builder().lockStatus(LockStatus.LOCK).build());
            notificationService.sendMaintenanceNotificationTelegram("ФулдисМдред нуждается в обслуживании (статус - " + response + ")");
        }
    }
}
