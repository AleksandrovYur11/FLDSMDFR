package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NotificationService.class})
@ExtendWith(SpringExtension.class)
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;

    /**
     * Method under test:
     * {@link NotificationService#sendFoodNotificationTelegram(String)}
     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testSendFoodNotificationTelegram() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.IllegalArgumentException: Not enough variable values available to expand 'notifications.food.telegram'
//        //       at ru.itmo.fldsmdfr.services.NotificationService.sendFoodNotificationTelegram(NotificationService.java:33)
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        // Arrange and Act
//        notificationService.sendFoodNotificationTelegram("Notification");
//    }

    /**
     * Method under test:
     * {@link NotificationService#sendFoodNotificationEmail(String)}
     */
    @Test
    void testSendFoodNotificationEmail() {
        // Arrange, Act and Assert
        assertThrows(RuntimeException.class, () -> notificationService.sendFoodNotificationEmail("Notification"));
    }

    /**
     * Method under test:
     * {@link NotificationService#sendEmailWithAttachments(String, String, String, String, String, String, String, String[])}
     */
    @Test
    void testSendEmailWithAttachments() throws UnknownHostException, MessagingException {
        try (MockedStatic<InetAddress> mockInetAddress = mockStatic(InetAddress.class)) {
            // Arrange
            mockInetAddress.when(() -> InetAddress.getByName(Mockito.<String>any()))
                    .thenThrow(new IllegalStateException("mail.smtp.host"));
            mockInetAddress.when(InetAddress::getLocalHost).thenThrow(new IllegalStateException("mail.smtp.host"));

            // Act and Assert
            assertThrows(IllegalStateException.class,
                    () -> NotificationService.sendEmailWithAttachments("localhost", "Port", "janedoe", "iloveyou",
                            "mail.smtp.host", "Hello from the Dreaming Spires", "Not all who wander are lost",
                            new String[]{"Attach Files"}));
            mockInetAddress.verify(InetAddress::getLocalHost);
        }
    }

    /**
     * Method under test:
     * {@link NotificationService#sendEmailWithAttachments(String, String, String, String, String, String, String, String[])}
     */
    @Test
    void testSendEmailWithAttachments2() throws UnknownHostException, MessagingException {
        try (MockedStatic<InetAddress> mockInetAddress = mockStatic(InetAddress.class)) {
            // Arrange
            mockInetAddress.when(() -> InetAddress.getByName(Mockito.<String>any()))
                    .thenThrow(new IllegalStateException("mail.smtp.host"));
            mockInetAddress.when(InetAddress::getLocalHost).thenThrow(new IllegalStateException("mail.smtp.host"));

            // Act and Assert
            assertThrows(IllegalStateException.class, () -> NotificationService.sendEmailWithAttachments("localhost", "Port",
                    "janedoe", "iloveyou", "mail.smtp.host", null, "Not all who wander are lost", new String[]{"Attach Files"}));
            mockInetAddress.verify(InetAddress::getLocalHost);
        }
    }

    /**
     * Method under test:
     * {@link NotificationService#sendEmailWithAttachments(String, String, String, String, String, String, String, String[])}
     */
    @Test
    void testSendEmailWithAttachments3() throws MessagingException {
        try (MockedStatic<Transport> mockTransport = mockStatic(Transport.class)) {
            // Arrange
            mockTransport.when(() -> Transport.send(Mockito.<Message>any())).thenAnswer(invocation -> null);

            // Act
            NotificationService.sendEmailWithAttachments("localhost", "Port", "janedoe", "iloveyou", "mail.smtp.host",
                    "Hello from the Dreaming Spires", "Not all who wander are lost", new String[]{"Attach Files"});

            // Assert
            mockTransport.verify(() -> Transport.send(Mockito.<Message>any()));
        }
    }

    /**
     * Method under test:
     * {@link NotificationService#sendMaintenanceNotificationTelegram(String)}
     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testSendMaintenanceNotificationTelegram() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.IllegalArgumentException: Not enough variable values available to expand 'notifications.maintenance.telegram'
//        //       at ru.itmo.fldsmdfr.services.NotificationService.sendMaintenanceNotificationTelegram(NotificationService.java:113)
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        // Arrange and Act
//        notificationService.sendMaintenanceNotificationTelegram("Notification");
//    }
}
