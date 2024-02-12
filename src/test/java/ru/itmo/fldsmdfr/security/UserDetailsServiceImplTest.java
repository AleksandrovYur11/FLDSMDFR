package ru.itmo.fldsmdfr.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.UserRepository;

@ContextConfiguration(classes = {UserDetailsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link UserDetailsServiceImpl#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setRole(Role.CITIZEN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByLogin(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        UserDetails actualLoadUserByUsernameResult = userDetailsServiceImpl.loadUserByUsername("janedoe");

        // Assert
        verify(userRepository).findByLogin(Mockito.<String>any());
        assertEquals("Login", actualLoadUserByUsernameResult.getUsername());
    }

    /**
     * Method under test: {@link UserDetailsServiceImpl#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByLogin(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("janedoe"));
        verify(userRepository).findByLogin(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserDetailsServiceImpl#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        // Arrange
        when(userRepository.findByLogin(Mockito.<String>any())).thenThrow(new UsernameNotFoundException("Msg"));

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("janedoe"));
        verify(userRepository).findByLogin(Mockito.<String>any());
    }
}
