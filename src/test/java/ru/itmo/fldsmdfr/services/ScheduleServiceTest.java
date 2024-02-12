package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.repositories.DeliveryRepository;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.FldsmdfrLocksRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;

@ContextConfiguration(classes = {ScheduleService.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ScheduleServiceTest {
    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ScheduleService scheduleService;

    @MockBean
    private SkyDeviceService skyDeviceService;

    @MockBean
    private VoteService voteService;

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ScheduleService#setBreakfastCron(String)}
     *   <li>{@link ScheduleService#setDeviceCheckCron(String)}
     *   <li>{@link ScheduleService#setDinnerCron(String)}
     *   <li>{@link ScheduleService#setLunchCron(String)}
     *   <li>{@link ScheduleService#setVoteEndCron(String)}
     *   <li>{@link ScheduleService#getBreakfastCron()}
     *   <li>{@link ScheduleService#getDeviceCheckCron()}
     *   <li>{@link ScheduleService#getDinnerCron()}
     *   <li>{@link ScheduleService#getLunchCron()}
     *   <li>{@link ScheduleService#getVoteEndCron()}
     *   <li>{@link ScheduleService#getVoteStartCron()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        LockService lockService = new LockService(mock(FldsmdfrLocksRepository.class));
        SkyDeviceService skyDeviceService = new SkyDeviceService("https://example.org/example", lockService,
                new NotificationService());

        VoteService voteService = new VoteService(mock(VoteRepository.class), mock(DishRepository.class));

        NotificationService notificationService = new NotificationService();
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        ScheduleService scheduleService = new ScheduleService(skyDeviceService, voteService, notificationService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        // Act
        scheduleService.setBreakfastCron("Breakfast Cron");
        scheduleService.setDeviceCheckCron("Device Check Cron");
        scheduleService.setDinnerCron("Dinner Cron");
        scheduleService.setLunchCron("Lunch Cron");
        scheduleService.setVoteEndCron("Vote End Cron");
        String actualBreakfastCron = scheduleService.getBreakfastCron();
        String actualDeviceCheckCron = scheduleService.getDeviceCheckCron();
        String actualDinnerCron = scheduleService.getDinnerCron();
        String actualLunchCron = scheduleService.getLunchCron();
        String actualVoteEndCron = scheduleService.getVoteEndCron();
        scheduleService.getVoteStartCron();

        // Assert that nothing has changed
        assertEquals("Breakfast Cron", actualBreakfastCron);
        assertEquals("Device Check Cron", actualDeviceCheckCron);
        assertEquals("Dinner Cron", actualDinnerCron);
        assertEquals("Lunch Cron", actualLunchCron);
        assertEquals("Vote End Cron", actualVoteEndCron);
    }

    /**
     * Method under test: {@link ScheduleService#setVoteStartCron(String)}
     */
    @Test
    void testSetVoteStartCron() {
        // Arrange and Act
        scheduleService.setVoteStartCron("Vote Start Cron");

        // Assert
        assertEquals("Vote Start Cron", scheduleService.getVoteStartCron());
    }
}
