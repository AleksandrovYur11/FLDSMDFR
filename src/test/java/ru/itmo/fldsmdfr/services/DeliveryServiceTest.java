package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.dto.DeliveryFinishedDto;
import ru.itmo.fldsmdfr.dto.DeliveryProgressDto;
import ru.itmo.fldsmdfr.models.Delivery;
import ru.itmo.fldsmdfr.models.DeliveryStatus;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DeliveryRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;

@ContextConfiguration(classes = {DeliveryService.class})
@ExtendWith(SpringExtension.class)
class DeliveryServiceTest {
    @MockBean
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryService deliveryService;

    @MockBean
    private VoteRepository voteRepository;

    @MockBean
    private VoteService voteService;

    /**
     * Method under test: {@link DeliveryService#getAllDeliveries()}
     */
    @Test
    void testGetAllDeliveries() {
        // Arrange
        ArrayList<Delivery> deliveryList = new ArrayList<>();
        when(deliveryRepository.findAll()).thenReturn(deliveryList);

        // Act
        List<Delivery> actualAllDeliveries = deliveryService.getAllDeliveries();

        // Assert
        verify(deliveryRepository).findAll();
        assertTrue(actualAllDeliveries.isEmpty());
        assertSame(deliveryList, actualAllDeliveries);
    }

    /**
     * Method under test: {@link DeliveryService#getAllDeliveries()}
     */
    @Test
    void testGetAllDeliveries2() {
        // Arrange
        when(deliveryRepository.findAll()).thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> deliveryService.getAllDeliveries());
        verify(deliveryRepository).findAll();
    }

    /**
     * Method under test:
     * {@link DeliveryService#createDeliveriesForWinners(LocalDate)}
     */
    @Test
    void testCreateDeliveriesForWinners() {
        // Arrange
        when(voteRepository.findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any())).thenReturn(new ArrayList<>());

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");
        when(voteService.getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any())).thenReturn(dish);

        // Act
        deliveryService.createDeliveriesForWinners(LocalDate.of(1970, 1, 1));

        // Assert that nothing has changed
        verify(voteRepository, atLeast(1)).findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any());
        verify(voteService, atLeast(1)).getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any());
    }

    /**
     * Method under test:
     * {@link DeliveryService#createDeliveriesForWinners(LocalDate)}
     */
    @Test
    void testCreateDeliveriesForWinners2() {
        // Arrange
        when(voteService.getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> deliveryService.createDeliveriesForWinners(LocalDate.of(1970, 1, 1)));
        verify(voteService).getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any());
    }

    /**
     * Method under test:
     * {@link DeliveryService#createDeliveriesForWinners(LocalDate)}
     */
    @Test
    void testCreateDeliveriesForWinners3() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

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
        vote.setDish(dish2);
        vote.setFoodTime(FoodTime.BREAKFAST);
        vote.setId(1L);
        vote.setUser(user);

        ArrayList<Vote> voteList = new ArrayList<>();
        voteList.add(vote);
        when(voteRepository.findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any())).thenReturn(voteList);

        Dish dish3 = new Dish();
        dish3.setId(1L);
        dish3.setName("Name");
        when(voteService.getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any())).thenReturn(dish3);

        // Act
        deliveryService.createDeliveriesForWinners(LocalDate.of(1970, 1, 1));

        // Assert
        verify(deliveryRepository, atLeast(1)).save(Mockito.<Delivery>any());
        verify(voteRepository, atLeast(1)).findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any());
        verify(voteService, atLeast(1)).getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any());
    }

    /**
     * Method under test:
     * {@link DeliveryService#createDeliveriesForWinners(LocalDate)}
     */
    @Test
    void testCreateDeliveriesForWinners4() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

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
        vote.setDish(dish2);
        vote.setFoodTime(FoodTime.BREAKFAST);
        vote.setId(1L);
        vote.setUser(user);

        Dish dish3 = new Dish();
        dish3.setId(2L);
        dish3.setName("42");

        User user2 = new User();
        user2.setAddress("17 High St");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setLogin("42");
        user2.setPassword("Password");
        user2.setRole(Role.DELIVERYMAN);

        Vote vote2 = new Vote();
        vote2.setDate(LocalDate.of(1970, 1, 1));
        vote2.setDish(dish3);
        vote2.setFoodTime(FoodTime.LUNCH);
        vote2.setId(2L);
        vote2.setUser(user2);

        ArrayList<Vote> voteList = new ArrayList<>();
        voteList.add(vote2);
        voteList.add(vote);
        when(voteRepository.findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any())).thenReturn(voteList);

        Dish dish4 = new Dish();
        dish4.setId(1L);
        dish4.setName("Name");
        when(voteService.getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any())).thenReturn(dish4);

        // Act
        deliveryService.createDeliveriesForWinners(LocalDate.of(1970, 1, 1));

        // Assert
        verify(deliveryRepository, atLeast(1)).save(Mockito.<Delivery>any());
        verify(voteRepository, atLeast(1)).findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any());
        verify(voteService, atLeast(1)).getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any());
    }

    /**
     * Method under test:
     * {@link DeliveryService#createDeliveriesForWinners(LocalDate)}
     */
    @Test
    void testCreateDeliveriesForWinners5() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

        User user = new User();
        user.setAddress("42 Main St");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setRole(Role.CITIZEN);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setLogin("Login");
        user2.setPassword("iloveyou");
        user2.setRole(Role.CITIZEN);

        Dish dish3 = new Dish();
        dish3.setId(1L);
        dish3.setName("Name");
        Vote vote = mock(Vote.class);
        when(vote.getDate()).thenReturn(LocalDate.of(1970, 1, 1));
        when(vote.getDish()).thenReturn(dish3);
        when(vote.getFoodTime()).thenReturn(FoodTime.BREAKFAST);
        when(vote.getUser()).thenReturn(user2);
        doNothing().when(vote).setDate(Mockito.<LocalDate>any());
        doNothing().when(vote).setDish(Mockito.<Dish>any());
        doNothing().when(vote).setFoodTime(Mockito.<FoodTime>any());
        doNothing().when(vote).setId(Mockito.<Long>any());
        doNothing().when(vote).setUser(Mockito.<User>any());
        vote.setDate(LocalDate.of(1970, 1, 1));
        vote.setDish(dish2);
        vote.setFoodTime(FoodTime.BREAKFAST);
        vote.setId(1L);
        vote.setUser(user);

        ArrayList<Vote> voteList = new ArrayList<>();
        voteList.add(vote);
        when(voteRepository.findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any())).thenReturn(voteList);

        Dish dish4 = new Dish();
        dish4.setId(1L);
        dish4.setName("Name");
        when(voteService.getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any())).thenReturn(dish4);

        // Act
        deliveryService.createDeliveriesForWinners(LocalDate.of(1970, 1, 1));

        // Assert
        verify(deliveryRepository, atLeast(1)).save(Mockito.<Delivery>any());
        verify(vote, atLeast(1)).getDate();
        verify(vote, atLeast(1)).getDish();
        verify(vote, atLeast(1)).getFoodTime();
        verify(vote, atLeast(1)).getUser();
        verify(vote).setDate(Mockito.<LocalDate>any());
        verify(vote).setDish(Mockito.<Dish>any());
        verify(vote).setFoodTime(Mockito.<FoodTime>any());
        verify(vote).setId(Mockito.<Long>any());
        verify(vote).setUser(Mockito.<User>any());
        verify(voteRepository, atLeast(1)).findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any());
        verify(voteService, atLeast(1)).getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any());
    }

    /**
     * Method under test:
     * {@link DeliveryService#createDeliveriesForWinners(LocalDate)}
     */
    @Test
    void testCreateDeliveriesForWinners6() {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setLogin("Login");
        user2.setPassword("iloveyou");
        user2.setRole(Role.CITIZEN);
        Vote vote = mock(Vote.class);
        when(vote.getFoodTime()).thenThrow(new IllegalArgumentException("foo"));
        when(vote.getUser()).thenReturn(user2);
        doNothing().when(vote).setDate(Mockito.<LocalDate>any());
        doNothing().when(vote).setDish(Mockito.<Dish>any());
        doNothing().when(vote).setFoodTime(Mockito.<FoodTime>any());
        doNothing().when(vote).setId(Mockito.<Long>any());
        doNothing().when(vote).setUser(Mockito.<User>any());
        vote.setDate(LocalDate.of(1970, 1, 1));
        vote.setDish(dish);
        vote.setFoodTime(FoodTime.BREAKFAST);
        vote.setId(1L);
        vote.setUser(user);

        ArrayList<Vote> voteList = new ArrayList<>();
        voteList.add(vote);
        when(voteRepository.findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any())).thenReturn(voteList);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");
        when(voteService.getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any())).thenReturn(dish2);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> deliveryService.createDeliveriesForWinners(LocalDate.of(1970, 1, 1)));
        verify(vote).getFoodTime();
        verify(vote).getUser();
        verify(vote).setDate(Mockito.<LocalDate>any());
        verify(vote).setDish(Mockito.<Dish>any());
        verify(vote).setFoodTime(Mockito.<FoodTime>any());
        verify(vote).setId(Mockito.<Long>any());
        verify(vote).setUser(Mockito.<User>any());
        verify(voteRepository, atLeast(1)).findByDishAndDate(Mockito.<Dish>any(), Mockito.<LocalDate>any());
        verify(voteService, atLeast(1)).getWinnerDish(Mockito.<LocalDate>any(), Mockito.<FoodTime>any());
    }

    /**
     * Method under test: {@link DeliveryService#setInProgress(DeliveryProgressDto)}
     */
    @Test
    void testSetInProgress() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        Optional<Delivery> ofResult = Optional.of(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

        Delivery delivery2 = new Delivery();
        delivery2.setAddress("42 Main St");
        delivery2.setDate(LocalDate.of(1970, 1, 1));
        delivery2.setDish(dish2);
        delivery2.setFoodTime(FoodTime.BREAKFAST);
        delivery2.setId(1L);
        delivery2.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery2.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery2);
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        deliveryService.setInProgress(new DeliveryProgressDto());

        // Assert
        verify(deliveryRepository).findById(Mockito.<Long>any());
        verify(deliveryRepository).save(Mockito.<Delivery>any());
    }

    /**
     * Method under test: {@link DeliveryService#setInProgress(DeliveryProgressDto)}
     */
    @Test
    void testSetInProgress2() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        Optional<Delivery> ofResult = Optional.of(delivery);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenThrow(new IllegalArgumentException("foo"));
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> deliveryService.setInProgress(new DeliveryProgressDto()));
        verify(deliveryRepository).findById(Mockito.<Long>any());
        verify(deliveryRepository).save(Mockito.<Delivery>any());
    }

    /**
     * Method under test: {@link DeliveryService#setInProgress(DeliveryProgressDto)}
     */
    @Test
    void testSetInProgress3() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");
        Delivery delivery = mock(Delivery.class);
        when(delivery.getStatus()).thenReturn(DeliveryStatus.NEW);
        doNothing().when(delivery).setAddress(Mockito.<String>any());
        doNothing().when(delivery).setDate(Mockito.<LocalDate>any());
        doNothing().when(delivery).setDish(Mockito.<Dish>any());
        doNothing().when(delivery).setFoodTime(Mockito.<FoodTime>any());
        doNothing().when(delivery).setId(Mockito.<Long>any());
        doNothing().when(delivery).setLastChangeTimestamp(Mockito.<Instant>any());
        doNothing().when(delivery).setStatus(Mockito.<DeliveryStatus>any());
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        Optional<Delivery> ofResult = Optional.of(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

        Delivery delivery2 = new Delivery();
        delivery2.setAddress("42 Main St");
        delivery2.setDate(LocalDate.of(1970, 1, 1));
        delivery2.setDish(dish2);
        delivery2.setFoodTime(FoodTime.BREAKFAST);
        delivery2.setId(1L);
        delivery2.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery2.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery2);
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        deliveryService.setInProgress(new DeliveryProgressDto());

        // Assert that nothing has changed
        verify(deliveryRepository).findById(Mockito.<Long>any());
        verify(deliveryRepository).save(Mockito.<Delivery>any());
        verify(delivery).getStatus();
        verify(delivery).setAddress(Mockito.<String>any());
        verify(delivery).setDate(Mockito.<LocalDate>any());
        verify(delivery).setDish(Mockito.<Dish>any());
        verify(delivery).setFoodTime(Mockito.<FoodTime>any());
        verify(delivery).setId(Mockito.<Long>any());
        verify(delivery, atLeast(1)).setLastChangeTimestamp(Mockito.<Instant>any());
        verify(delivery, atLeast(1)).setStatus(Mockito.<DeliveryStatus>any());
    }

    /**
     * Method under test: {@link DeliveryService#setInProgress(DeliveryProgressDto)}
     */
    @Test
    void testSetInProgress4() {
        // Arrange
        Optional<Delivery> emptyResult = Optional.empty();
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> deliveryService.setInProgress(new DeliveryProgressDto()));
        verify(deliveryRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DeliveryService#setDelivered(DeliveryFinishedDto)}
     */
    @Test
    void testSetDelivered() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        Optional<Delivery> ofResult = Optional.of(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

        Delivery delivery2 = new Delivery();
        delivery2.setAddress("42 Main St");
        delivery2.setDate(LocalDate.of(1970, 1, 1));
        delivery2.setDish(dish2);
        delivery2.setFoodTime(FoodTime.BREAKFAST);
        delivery2.setId(1L);
        delivery2.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery2.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery2);
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        deliveryService.setDelivered(new DeliveryFinishedDto());

        // Assert
        verify(deliveryRepository).findById(Mockito.<Long>any());
        verify(deliveryRepository).save(Mockito.<Delivery>any());
    }

    /**
     * Method under test: {@link DeliveryService#setDelivered(DeliveryFinishedDto)}
     */
    @Test
    void testSetDelivered2() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");

        Delivery delivery = new Delivery();
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        Optional<Delivery> ofResult = Optional.of(delivery);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenThrow(new IllegalArgumentException("foo"));
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> deliveryService.setDelivered(new DeliveryFinishedDto()));
        verify(deliveryRepository).findById(Mockito.<Long>any());
        verify(deliveryRepository).save(Mockito.<Delivery>any());
    }

    /**
     * Method under test: {@link DeliveryService#setDelivered(DeliveryFinishedDto)}
     */
    @Test
    void testSetDelivered3() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");
        Delivery delivery = mock(Delivery.class);
        when(delivery.getStatus()).thenReturn(DeliveryStatus.NEW);
        doNothing().when(delivery).setAddress(Mockito.<String>any());
        doNothing().when(delivery).setDate(Mockito.<LocalDate>any());
        doNothing().when(delivery).setDish(Mockito.<Dish>any());
        doNothing().when(delivery).setFoodTime(Mockito.<FoodTime>any());
        doNothing().when(delivery).setId(Mockito.<Long>any());
        doNothing().when(delivery).setLastChangeTimestamp(Mockito.<Instant>any());
        doNothing().when(delivery).setStatus(Mockito.<DeliveryStatus>any());
        delivery.setAddress("42 Main St");
        delivery.setDate(LocalDate.of(1970, 1, 1));
        delivery.setDish(dish);
        delivery.setFoodTime(FoodTime.BREAKFAST);
        delivery.setId(1L);
        delivery.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery.setStatus(DeliveryStatus.NEW);
        Optional<Delivery> ofResult = Optional.of(delivery);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("Name");

        Delivery delivery2 = new Delivery();
        delivery2.setAddress("42 Main St");
        delivery2.setDate(LocalDate.of(1970, 1, 1));
        delivery2.setDish(dish2);
        delivery2.setFoodTime(FoodTime.BREAKFAST);
        delivery2.setId(1L);
        delivery2.setLastChangeTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        delivery2.setStatus(DeliveryStatus.NEW);
        when(deliveryRepository.save(Mockito.<Delivery>any())).thenReturn(delivery2);
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        deliveryService.setDelivered(new DeliveryFinishedDto());

        // Assert that nothing has changed
        verify(deliveryRepository).findById(Mockito.<Long>any());
        verify(deliveryRepository).save(Mockito.<Delivery>any());
        verify(delivery).getStatus();
        verify(delivery).setAddress(Mockito.<String>any());
        verify(delivery).setDate(Mockito.<LocalDate>any());
        verify(delivery).setDish(Mockito.<Dish>any());
        verify(delivery).setFoodTime(Mockito.<FoodTime>any());
        verify(delivery).setId(Mockito.<Long>any());
        verify(delivery, atLeast(1)).setLastChangeTimestamp(Mockito.<Instant>any());
        verify(delivery, atLeast(1)).setStatus(Mockito.<DeliveryStatus>any());
    }

    /**
     * Method under test: {@link DeliveryService#setDelivered(DeliveryFinishedDto)}
     */
    @Test
    void testSetDelivered4() {
        // Arrange
        Optional<Delivery> emptyResult = Optional.empty();
        when(deliveryRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> deliveryService.setDelivered(new DeliveryFinishedDto()));
        verify(deliveryRepository).findById(Mockito.<Long>any());
    }
}
