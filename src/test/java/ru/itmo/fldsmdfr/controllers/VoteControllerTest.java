package ru.itmo.fldsmdfr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.itmo.fldsmdfr.dto.UserVoteDto;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DishService;
import ru.itmo.fldsmdfr.services.VoteService;

class VoteControllerTest {
    /**
     * Method under test: {@link VoteController#vote(Map, UserDetailsImpl)}
     */
    @Test
    void testVote() {

        // Arrange
        VoteService voteService = new VoteService(mock(VoteRepository.class), mock(DishRepository.class));

        VoteController voteController = new VoteController(voteService, new DishService(mock(DishRepository.class)));
        HashMap<String, String> allParams = new HashMap<>();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> voteController.vote(allParams, new UserDetailsImpl(new User())));
    }

    /**
     * Method under test: {@link VoteController#vote(Map, UserDetailsImpl)}
     */
    @Test
    void testVote2() {

        // Arrange
        VoteService voteService = new VoteService(mock(VoteRepository.class), mock(DishRepository.class));

        VoteController voteController = new VoteController(voteService, new DishService(mock(DishRepository.class)));

        HashMap<String, String> allParams = new HashMap<>();
        allParams.put("breakfast", "breakfast");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> voteController.vote(allParams, new UserDetailsImpl(new User())));
    }

    /**
     * Method under test: {@link VoteController#vote(Map, UserDetailsImpl)}
     */
    @Test
    void testVote3() {

        // Arrange
        VoteService voteService = new VoteService(mock(VoteRepository.class), mock(DishRepository.class));

        VoteController voteController = new VoteController(voteService, new DishService(mock(DishRepository.class)));

        HashMap<String, String> allParams = new HashMap<>();
        allParams.put("lunch", "lunch");
        allParams.put("breakfast", "breakfast");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> voteController.vote(allParams, new UserDetailsImpl(new User())));
    }

    /**
     * Method under test: {@link VoteController#vote(Map, UserDetailsImpl)}
     */
    @Test
    void testVote4() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        User user = new User();
        user.setAddress("42 Main St");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setRole(Role.CITIZEN);

        Vote vote = new Vote();
        vote.setDate(LocalDate.of(1970, 1, 1));
        vote.setDish(dish);
        vote.setFoodTime(FoodTime.BREAKFAST);
        vote.setId(1L);
        vote.setUser(user);
        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.save(Mockito.<Vote>any())).thenReturn(vote);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");
        Optional<Dish> ofResult = Optional.of(dish2);
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        VoteService voteService = new VoteService(voteRepository, dishRepository);

        VoteController voteController = new VoteController(voteService, new DishService(mock(DishRepository.class)));

        HashMap<String, String> allParams = new HashMap<>();
        allParams.put((String) "breakfast", "foo");
        allParams.put((String) "lunch", "foo");
        allParams.put((String) "dinner", "foo");
        allParams.put((String) "breakfast", "42");
        allParams.put((String) "lunch", "42");
        allParams.put((String) "dinner", "42");

        // Act
        String actualVoteResult = voteController.vote(allParams, new UserDetailsImpl(new User()));

        // Assert
        verify(dishRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(voteRepository, atLeast(1)).save(Mockito.<Vote>any());
        assertEquals("redirect:/cabinet", actualVoteResult);
    }

    /**
     * Method under test: {@link VoteController#vote(Map, UserDetailsImpl)}
     */
    @Test
    void testVote5() {
        // Arrange
        VoteService voteService = mock(VoteService.class);
        doNothing().when(voteService).saveVote(Mockito.<UserVoteDto>any());
        VoteController voteController = new VoteController(voteService, new DishService(mock(DishRepository.class)));

        HashMap<String, String> allParams = new HashMap<>();
        allParams.put((String) "breakfast", "foo");
        allParams.put((String) "lunch", "foo");
        allParams.put((String) "dinner", "foo");
        allParams.put((String) "breakfast", "42");
        allParams.put((String) "lunch", "42");
        allParams.put((String) "dinner", "42");

        // Act
        String actualVoteResult = voteController.vote(allParams, new UserDetailsImpl(new User()));

        // Assert
        verify(voteService).saveVote(Mockito.<UserVoteDto>any());
        assertEquals("redirect:/cabinet", actualVoteResult);
    }
}
