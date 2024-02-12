package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.dto.UserRegistrationDto;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.UserRepository;

@ContextConfiguration(classes = {RegistrationService.class})
@ExtendWith(SpringExtension.class)
class RegistrationServiceTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link RegistrationService#register(UserRegistrationDto)}
     */
    @Test
    void testRegister() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        User user = new User();
        user.setAddress("42 Main St");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setRole(Role.CITIZEN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setLogin("Login");
        user2.setPassword("iloveyou");
        user2.setRole(Role.CITIZEN);
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().user(user2).build();

        // Act
        registrationService.register(userRegistrationDto);

        // Assert
        verify(userRepository).save(Mockito.<User>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
        assertEquals("secret", userRegistrationDto.getUser().getPassword());
    }
}
