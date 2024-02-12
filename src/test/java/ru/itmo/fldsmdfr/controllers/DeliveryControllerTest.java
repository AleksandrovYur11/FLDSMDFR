package ru.itmo.fldsmdfr.controllers;

import static org.mockito.Mockito.doNothing;

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
import ru.itmo.fldsmdfr.dto.DeliveryFinishedDto;
import ru.itmo.fldsmdfr.dto.DeliveryProgressDto;
import ru.itmo.fldsmdfr.services.DeliveryService;

@ContextConfiguration(classes = {DeliveryController.class})
@ExtendWith(SpringExtension.class)
class DeliveryControllerTest {
    @Autowired
    private DeliveryController deliveryController;

    @MockBean
    private DeliveryService deliveryService;

    /**
     * Method under test: {@link DeliveryController#setInProgress(Long)}
     */
    @Test
    void testSetInProgress() throws Exception {
        // Arrange
        doNothing().when(deliveryService).setInProgress(Mockito.<DeliveryProgressDto>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delivery/setprogress/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(deliveryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/cabinet"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cabinet"));
    }

    /**
     * Method under test: {@link DeliveryController#setInProgress(Long)}
     */
    @Test
    void testSetInProgress2() throws Exception {
        // Arrange
        doNothing().when(deliveryService).setInProgress(Mockito.<DeliveryProgressDto>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delivery/setprogress/{id}", 1L);
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(deliveryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/cabinet"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cabinet"));
    }

    /**
     * Method under test: {@link DeliveryController#setDelivered(Long)}
     */
    @Test
    void testSetDelivered() throws Exception {
        // Arrange
        doNothing().when(deliveryService).setDelivered(Mockito.<DeliveryFinishedDto>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delivery/setdelivered/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(deliveryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/cabinet"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cabinet"));
    }

    /**
     * Method under test: {@link DeliveryController#setDelivered(Long)}
     */
    @Test
    void testSetDelivered2() throws Exception {
        // Arrange
        doNothing().when(deliveryService).setDelivered(Mockito.<DeliveryFinishedDto>any());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(deliveryController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
