package ru.itmo.fldsmdfr.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import ru.itmo.fldsmdfr.services.ScheduleService;
import ru.itmo.fldsmdfr.services.VoteService;

@ContextConfiguration(classes = {CronDebugController.class})
@ExtendWith(SpringExtension.class)
class CronDebugControllerTest {
    @MockBean
    private ContextRefresher contextRefresher;

    @Autowired
    private CronDebugController cronDebugController;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private VoteService voteService;

    /**
     * Method under test: {@link CronDebugController#getCronDebugPage(Model)}
     */
    @Test
    void testGetCronDebugPage() throws Exception {
        // Arrange
        when(scheduleService.getBreakfastCron()).thenReturn("Breakfast Cron");
        when(scheduleService.getDeviceCheckCron()).thenReturn("Device Check Cron");
        when(scheduleService.getDinnerCron()).thenReturn("Dinner Cron");
        when(scheduleService.getLunchCron()).thenReturn("Lunch Cron");
        when(scheduleService.getVoteEndCron()).thenReturn("Vote End Cron");
        when(scheduleService.getVoteStartCron()).thenReturn("Vote Start Cron");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/crondebug");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cronDebugController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.model()
                        .attributeExists("breakfastCron", "deviceCheckCron", "dinnerCron", "lunchCron", "voteEndCron",
                                "voteStartCron"))
                .andExpect(MockMvcResultMatchers.view().name("cronDebug"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("cronDebug"));
    }

    /**
     * Method under test: {@link CronDebugController#getCronDebugPage(Model)}
     */
    @Test
    void testGetCronDebugPageUnauthorized() throws Exception {
        // Arrange
        when(scheduleService.getBreakfastCron()).thenReturn("Breakfast Cron");
        when(scheduleService.getDeviceCheckCron()).thenReturn("Device Check Cron");
        when(scheduleService.getDinnerCron()).thenReturn("Dinner Cron");
        when(scheduleService.getLunchCron()).thenReturn("Lunch Cron");
        when(scheduleService.getVoteEndCron()).thenReturn("Vote End Cron");
        when(scheduleService.getVoteStartCron()).thenReturn("Vote Start Cron");
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cronDebugController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link CronDebugController#setCronDebug(Map)}
     */
    @Test
    void testSetCronDebugRedirectOnForm() throws Exception {
        // Arrange
        doNothing().when(scheduleService).setBreakfastCron(Mockito.<String>any());
        doNothing().when(scheduleService).setDeviceCheckCron(Mockito.<String>any());
        doNothing().when(scheduleService).setDinnerCron(Mockito.<String>any());
        doNothing().when(scheduleService).setLunchCron(Mockito.<String>any());
        doNothing().when(scheduleService).setVoteEndCron(Mockito.<String>any());
        doNothing().when(scheduleService).setVoteStartCron(Mockito.<String>any());
        when(contextRefresher.refresh()).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/crondebug");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cronDebugController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/crondebug"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/crondebug"));
    }
}
