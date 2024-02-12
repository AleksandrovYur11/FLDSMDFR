package ru.itmo.fldsmdfr.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.itmo.fldsmdfr.dto.LockStatusDto;
import ru.itmo.fldsmdfr.services.LockService;

@ContextConfiguration(classes = {MaintenanceController.class})
@ExtendWith(SpringExtension.class)
class MaintenanceControllerTest {
    @MockBean
    private LockService lockService;

    @Autowired
    private MaintenanceController maintenanceController;

    /**
     * Method under test: {@link MaintenanceController#maintain()}
     */
    @Test
    void testMaintain() throws Exception {
        // Arrange
        doNothing().when(lockService).saveLock(Mockito.<LockStatusDto>any());
        when(lockService.isLocked()).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/maintain");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(maintenanceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/cabinet"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cabinet"));
    }

    /**
     * Method under test: {@link MaintenanceController#maintain()}
     */
    @Test
    void testMaintain2() throws Exception {
        // Arrange
        doNothing().when(lockService).saveLock(Mockito.<LockStatusDto>any());
        when(lockService.isLocked()).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/maintain");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(maintenanceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/cabinet"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cabinet"));
    }

    /**
     * Method under test: {@link MaintenanceController#maintain()}
     */
    @Test
    void testMaintain3() throws Exception {
        // Arrange
        doNothing().when(lockService).saveLock(Mockito.<LockStatusDto>any());
        when(lockService.isLocked()).thenReturn(true);
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(maintenanceController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
