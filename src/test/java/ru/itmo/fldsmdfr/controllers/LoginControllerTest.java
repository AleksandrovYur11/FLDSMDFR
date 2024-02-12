package ru.itmo.fldsmdfr.controllers;

import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.itmo.fldsmdfr.dto.UserRegistrationDto;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.services.RegistrationService;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
class LoginControllerTest {
  @Autowired
  private LoginController loginController;

  @MockBean
  private RegistrationService registrationService;

  /**
   * Method under test: {@link LoginController#getLoginPage()}
   */
  @Test
  void testGetLoginPage() throws Exception {
    // Arrange
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login");

    // Act and Assert
    MockMvcBuilders.standaloneSetup(loginController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().size(0))
            .andExpect(MockMvcResultMatchers.view().name("auth/login"))
            .andExpect(MockMvcResultMatchers.forwardedUrl("auth/login"));
  }

  /**
   * Method under test: {@link LoginController#getLoginPage()}
   */
  @Test
  void testGetLoginPage2() throws Exception {
    // Arrange
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login", "Uri Variables");

    // Act and Assert
    MockMvcBuilders.standaloneSetup(loginController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().size(0))
            .andExpect(MockMvcResultMatchers.view().name("auth/login"))
            .andExpect(MockMvcResultMatchers.forwardedUrl("auth/login"));
  }

  /**
   * Method under test: {@link LoginController#getRegistrationPage(User)}
   */
  @Test
  void testGetRegistrationPage() throws Exception {
    // Arrange
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/registration");

    // Act and Assert
    MockMvcBuilders.standaloneSetup(loginController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().size(1))
            .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
            .andExpect(MockMvcResultMatchers.view().name("auth/registration"))
            .andExpect(MockMvcResultMatchers.forwardedUrl("auth/registration"));
  }

  /**
   * Method under test: {@link LoginController#getRegistrationPage(User)}
   */
  @Test
  void testGetRegistrationPage2() throws Exception {
    // Arrange
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/registration");
    requestBuilder.contentType("https://example.org/example");

    // Act and Assert
    MockMvcBuilders.standaloneSetup(loginController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().size(1))
            .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
            .andExpect(MockMvcResultMatchers.view().name("auth/registration"))
            .andExpect(MockMvcResultMatchers.forwardedUrl("auth/registration"));
  }

  /**
   * Method under test: {@link LoginController#registerUser(User)}
   */
  @Test
  void testRegisterUser() throws Exception {
    // Arrange
    doNothing().when(registrationService).register(Mockito.<UserRegistrationDto>any());
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registration");

    // Act and Assert
    MockMvcBuilders.standaloneSetup(loginController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isFound())
            .andExpect(MockMvcResultMatchers.model().size(0))
            .andExpect(MockMvcResultMatchers.view().name("redirect:/login"))
            .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
  }
}
