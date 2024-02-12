package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.dto.UserVoteDto;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;

@ContextConfiguration(classes = {VoteService.class})
@ExtendWith(SpringExtension.class)
class VoteServiceTest {
    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private VoteRepository voteRepository;

    @Autowired
    private VoteService voteService;

    /**
     * Method under test: {@link VoteService#saveVote(UserVoteDto)}
     */
    @Test
    void testSaveVote() {
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
        when(voteRepository.save(Mockito.<Vote>any())).thenReturn(vote);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");
        Optional<Dish> ofResult = Optional.of(dish2);
        when(dishRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        voteService.saveVote(new UserVoteDto());

        // Assert
        verify(dishRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(voteRepository, atLeast(1)).save(Mockito.<Vote>any());
    }

    /**
     * Method under test: {@link VoteService#hasUserVotedToday(User)}
     */
    @Test
    void testHasUserVotedToday() {
        // Arrange
        when(voteRepository.findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setAddress("42 Main St");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setRole(Role.CITIZEN);

        // Act
        boolean actualHasUserVotedTodayResult = voteService.hasUserVotedToday(user);

        // Assert
        verify(voteRepository).findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any());
        assertFalse(actualHasUserVotedTodayResult);
    }

    /**
     * Method under test: {@link VoteService#hasUserVotedToday(User)}
     */
    @Test
    void testHasUserVotedToday2() {
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

        ArrayList<Vote> voteList = new ArrayList<>();
        voteList.add(vote);
        when(voteRepository.findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any())).thenReturn(voteList);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setLogin("Login");
        user2.setPassword("iloveyou");
        user2.setRole(Role.CITIZEN);

        // Act
        boolean actualHasUserVotedTodayResult = voteService.hasUserVotedToday(user2);

        // Assert
        verify(voteRepository).findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any());
        assertTrue(actualHasUserVotedTodayResult);
    }

//    /**
//     * Method under test: {@link VoteService#getWinnerDish(LocalDate, FoodTime)}
//     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testGetWinnerDish() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
//        //       at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
//        //       at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
//        //       at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
//        //       at java.base/java.util.Objects.checkIndex(Objects.java:385)
//        //       at java.base/java.util.ArrayList.get(ArrayList.java:427)
//        //       at ru.itmo.fldsmdfr.services.VoteService.getWinnerDish(VoteService.java:80)
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        // Arrange
//        when(voteRepository.findDishesGroupedByVoteCount(Mockito.<LocalDate>any(), Mockito.<FoodTime>any()))
//                .thenReturn(new ArrayList<>());
//
//        // Act
//        voteService.getWinnerDish(LocalDate.of(1970, 1, 1), FoodTime.BREAKFAST);
//    }

    /**
     * Method under test: {@link VoteService#getWinnerUsers(LocalDate, FoodTime)}
     */
    @Test
    void testGetWinnerUsers() {
        // Arrange, Act and Assert
        assertNull(voteService.getWinnerUsers(LocalDate.of(1970, 1, 1), FoodTime.BREAKFAST));
        assertNull(voteService.getWinnerUsers(LocalDate.of(1970, 1, 1), FoodTime.LUNCH));
        assertNull(voteService.getWinnerUsers(LocalDate.of(1970, 1, 1), FoodTime.DINNER));
    }

//    /**
//     * Method under test: {@link VoteService#getNextVoteStartDate()}
//     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testGetNextVoteStartDate() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.IllegalArgumentException: Cron expression must consist of 6 fields (found 1 in "${voteStartCron}")
//        //       at ru.itmo.fldsmdfr.services.VoteService.getNextVoteStartDate(VoteService.java:100)
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        // Arrange and Act
//        voteService.getNextVoteStartDate();
//    }

    /**
     * Method under test: {@link VoteService#getNextVoteEndDate()}
     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testGetNextVoteEndDate() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.IllegalArgumentException: Cron expression must consist of 6 fields (found 1 in "${voteEndCron}")
//        //       at ru.itmo.fldsmdfr.services.VoteService.getNextVoteEndDate(VoteService.java:105)
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        // Arrange and Act
//        voteService.getNextVoteEndDate();
//    }

//    /**
//     * Method under test: {@link VoteService#logTest()}
//     */
//    @Test
//    void testLogTest() {
//        // TODO: Complete this test.
//        //   Diffblue AI was unable to find a test
//
//        // Arrange and Act
//        voteService.logTest();
//    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link VoteService#setIsVoteActive(boolean)}
     *   <li>{@link VoteService#isVoteActive()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        VoteService voteService = new VoteService(mock(VoteRepository.class), mock(DishRepository.class));

        // Act
        voteService.setIsVoteActive(true);

        // Assert that nothing has changed
        assertTrue(voteService.isVoteActive());
    }
}
