package ru.itmo.fldsmdfr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FldsmdfrLock;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.LockStatus;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DeliveryRepository;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.FldsmdfrLocksRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.DeliveryService;
import ru.itmo.fldsmdfr.services.DishService;
import ru.itmo.fldsmdfr.services.LockService;
import ru.itmo.fldsmdfr.services.VoteService;

class CabinetControllerTest {
    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any())).thenReturn(new ArrayList<>());
        VoteService voteService = new VoteService(voteRepository, mock(DishRepository.class));

        FldsmdfrLock fldsmdfrLock = new FldsmdfrLock();
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        FldsmdfrLocksRepository locksRepository = mock(FldsmdfrLocksRepository.class);
        when(locksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);
        LockService lockService = new LockService(locksRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository2 = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository2,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(locksRepository).findTopBy(Mockito.<Sort>any());
        verify(voteRepository).findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any());
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("CITIZEN");

        User user = new User();
        user.setAddress("42 Main St");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setLogin("CITIZEN");
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
        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any())).thenReturn(voteList);
        VoteService voteService = new VoteService(voteRepository, mock(DishRepository.class));

        FldsmdfrLock fldsmdfrLock = new FldsmdfrLock();
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        FldsmdfrLocksRepository locksRepository = mock(FldsmdfrLocksRepository.class);
        when(locksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);
        LockService lockService = new LockService(locksRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository2 = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository2,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user2 = new User();
        user2.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user2);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(locksRepository).findTopBy(Mockito.<Sort>any());
        verify(voteRepository).findByDateAndUser(Mockito.<LocalDate>any(), Mockito.<User>any());
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        VoteService voteService = mock(VoteService.class);
        when(voteService.hasUserVotedToday(Mockito.<User>any())).thenReturn(true);
        when(voteService.isVoteActive()).thenReturn(true);

        FldsmdfrLock fldsmdfrLock = new FldsmdfrLock();
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        FldsmdfrLocksRepository locksRepository = mock(FldsmdfrLocksRepository.class);
        when(locksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);
        LockService lockService = new LockService(locksRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(locksRepository).findTopBy(Mockito.<Sort>any());
        verify(voteService).hasUserVotedToday(Mockito.<User>any());
        verify(voteService).isVoteActive();
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        VoteService voteService = mock(VoteService.class);
        when(voteService.hasUserVotedToday(Mockito.<User>any())).thenReturn(true);
        when(voteService.isVoteActive()).thenReturn(true);
        FldsmdfrLock fldsmdfrLock = mock(FldsmdfrLock.class);
        when(fldsmdfrLock.getType()).thenReturn(LockStatus.LOCK);
        doNothing().when(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        doNothing().when(fldsmdfrLock).setId(Mockito.<Long>any());
        doNothing().when(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        FldsmdfrLocksRepository locksRepository = mock(FldsmdfrLocksRepository.class);
        when(locksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);
        LockService lockService = new LockService(locksRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(fldsmdfrLock).getType();
        verify(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        verify(fldsmdfrLock).setId(Mockito.<Long>any());
        verify(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        verify(locksRepository).findTopBy(Mockito.<Sort>any());
        verify(voteService).hasUserVotedToday(Mockito.<User>any());
        verify(voteService).isVoteActive();
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet5() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        VoteService voteService = mock(VoteService.class);
        when(voteService.hasUserVotedToday(Mockito.<User>any())).thenReturn(true);
        when(voteService.isVoteActive()).thenReturn(true);
        FldsmdfrLock fldsmdfrLock = mock(FldsmdfrLock.class);
        when(fldsmdfrLock.getType()).thenReturn(LockStatus.UNLOCK);
        doNothing().when(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        doNothing().when(fldsmdfrLock).setId(Mockito.<Long>any());
        doNothing().when(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        FldsmdfrLocksRepository locksRepository = mock(FldsmdfrLocksRepository.class);
        when(locksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);
        LockService lockService = new LockService(locksRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(fldsmdfrLock).getType();
        verify(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        verify(fldsmdfrLock).setId(Mockito.<Long>any());
        verify(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        verify(locksRepository).findTopBy(Mockito.<Sort>any());
        verify(voteService).hasUserVotedToday(Mockito.<User>any());
        verify(voteService).isVoteActive();
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet6() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        VoteService voteService = mock(VoteService.class);
        when(voteService.hasUserVotedToday(Mockito.<User>any())).thenReturn(true);
        when(voteService.isVoteActive()).thenReturn(true);
        FldsmdfrLocksRepository locksRepository = mock(FldsmdfrLocksRepository.class);
        Optional<FldsmdfrLock> emptyResult = Optional.empty();
        when(locksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(emptyResult);
        LockService lockService = new LockService(locksRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(locksRepository).findTopBy(Mockito.<Sort>any());
        verify(voteService).hasUserVotedToday(Mockito.<User>any());
        verify(voteService).isVoteActive();
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        VoteService voteService = mock(VoteService.class);
        when(voteService.hasUserVotedToday(Mockito.<User>any())).thenReturn(true);
        when(voteService.isVoteActive()).thenReturn(true);
        LockService lockService = mock(LockService.class);
        when(lockService.isLocked()).thenReturn(true);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.CITIZEN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(dishRepository).findAll();
        verify(lockService).isLocked();
        verify(voteService).hasUserVotedToday(Mockito.<User>any());
        verify(voteService).isVoteActive();
        assertEquals("votesDishes", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet8() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishRepository dishRepository = mock(DishRepository.class);
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
        DishService dishService = new DishService(dishRepository);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        when(deliveryRepository.findAll()).thenReturn(new ArrayList<>());
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, mock(VoteService.class),
                mock(LockService.class), new DeliveryService(deliveryRepository, voteRepository,
                new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.DELIVERYMAN);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(deliveryRepository).findAll();
        assertEquals("addressDelivery", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet9() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        LockService lockService = mock(LockService.class);
        when(lockService.isLocked()).thenReturn(true);
        DishService dishService = new DishService(mock(DishRepository.class));
        VoteService voteService = mock(VoteService.class);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        User user = new User();
        user.setRole(Role.SCIENTIST);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(lockService).isLocked();
        assertEquals("maintenance", actualCabinetResult);
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet10() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishService dishService = new DishService(mock(DishRepository.class));
        VoteService voteService = mock(VoteService.class);
        LockService lockService = mock(LockService.class);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        Mockito.<Collection<? extends GrantedAuthority>>when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> cabinetController.cabinet(userDetails, new ConcurrentModel()));
        verify(userDetails).getAuthorities();
    }

    /**
     * Method under test: {@link CabinetController#cabinet(UserDetailsImpl, Model)}
     */
    @Test
    void testCabinet11() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "ru.itmo.fldsmdfr.models.User.getRole()" because "this.user" is null
        //       at ru.itmo.fldsmdfr.security.UserDetailsImpl.getAuthorities(UserDetailsImpl.java:21)
        //       at ru.itmo.fldsmdfr.controllers.CabinetController.cabinet(CabinetController.java:38)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        DishService dishService = new DishService(mock(DishRepository.class));
        VoteService voteService = mock(VoteService.class);
        LockService lockService = mock(LockService.class);
        DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
        VoteRepository voteRepository = mock(VoteRepository.class);
        CabinetController cabinetController = new CabinetController(dishService, voteService, lockService,
                new DeliveryService(deliveryRepository, voteRepository,
                        new VoteService(mock(VoteRepository.class), mock(DishRepository.class))));

        ArrayList<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("user has no role"));
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        Mockito.<Collection<? extends GrantedAuthority>>when(userDetails.getAuthorities()).thenReturn(grantedAuthorityList);

        // Act
        String actualCabinetResult = cabinetController.cabinet(userDetails, new ConcurrentModel());

        // Assert
        verify(userDetails).getAuthorities();
        assertEquals("error", actualCabinetResult);
    }
}
