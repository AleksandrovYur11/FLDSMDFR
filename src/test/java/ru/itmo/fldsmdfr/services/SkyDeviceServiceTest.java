package ru.itmo.fldsmdfr.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.dto.LockStatusDto;

@ContextConfiguration(classes = {SkyDeviceService.class, String.class})
@ExtendWith(SpringExtension.class)
class SkyDeviceServiceTest {
    @MockBean
    private LockService lockService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private SkyDeviceService skyDeviceService;

    /**
     * Method under test: {@link SkyDeviceService#updateStatusAndLock()}
     */
    @Test
    void testUpdateStatusAndLock() {
        // Arrange
        when(lockService.isLocked()).thenReturn(true);

        // Act
        skyDeviceService.updateStatusAndLock();

        // Assert
        verify(lockService).isLocked();
    }

    /**
     * Method under test: {@link SkyDeviceService#updateStatusAndLock()}
     */
    @Test
    void testUpdateStatusAndLock2() {
        // Arrange
        when(lockService.isLocked()).thenReturn(false);
        doNothing().when(lockService).saveLock(Mockito.<LockStatusDto>any());
        doNothing().when(notificationService).sendMaintenanceNotificationTelegram(Mockito.<String>any());

        // Act
        skyDeviceService.updateStatusAndLock();

        // Assert
        verify(lockService).isLocked();
        verify(lockService).saveLock(Mockito.<LockStatusDto>any());
        verify(notificationService).sendMaintenanceNotificationTelegram(Mockito.<String>any());
    }
}
