package ru.itmo.fldsmdfr.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itmo.fldsmdfr.models.*;
import ru.itmo.fldsmdfr.repositories.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
public class DbInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DishRepository dishRepository;
    private final FldsmdfrLocksRepository locksRepository;
    private final DeliveryRepository deliveryRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public DbInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, DishRepository dishRepository, FldsmdfrLocksRepository fldsmdfrLocksRepository, DeliveryRepository deliveryRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dishRepository = dishRepository;
        this.locksRepository = fldsmdfrLocksRepository;
        this.deliveryRepository = deliveryRepository;
        this.voteRepository = voteRepository;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void addUsersAfterStartup() {
        Optional<User> citizenUserOptional = userRepository.findByLogin("citizen");
        Optional<User> deliverymanUserOptional = userRepository.findByLogin("deliveryman");
        Optional<User> scientistUserOptional = userRepository.findByLogin("scientist");

        if (citizenUserOptional.isEmpty()) {
            userRepository.save(User.builder()
                    .login("citizen")
                    .password(passwordEncoder.encode("citizen"))
                    .role(Role.CITIZEN)
                    .address("Кронверкский 49")
                    .firstName("житель")
                    .lastName("простой")
                    .build());
            userRepository.save(User.builder()
                    .login("citizen2")
                    .password(passwordEncoder.encode("citizen2"))
                    .role(Role.CITIZEN)
                    .address("Ололоева 8")
                    .firstName("житель")
                    .lastName("еще один")
                    .build());
            userRepository.save(User.builder()
                    .login("citizen3")
                    .password(passwordEncoder.encode("citizen3"))
                    .role(Role.CITIZEN)
                    .address("Далеко...")
                    .firstName("житель")
                    .lastName("и еще третий")
                    .build());
        }
        if (deliverymanUserOptional.isEmpty()) {
            userRepository.save(User.builder()
                    .login("deliveryman")
                    .password(passwordEncoder.encode("deliveryman"))
                    .role(Role.DELIVERYMAN)
                    .firstName("доставщик")
                    .lastName("работящий")
                    .build());
        }
        if (scientistUserOptional.isEmpty()) {
            userRepository.save(User.builder()
                    .login("scientist")
                    .password(passwordEncoder.encode("scientist"))
                    .role(Role.SCIENTIST)
                    .firstName("ученый")
                    .lastName("гениальный")
                    .build());
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void addDishesAfterStartup() {
        Dish meatballs = Dish.builder()
                .name("Фрикадельки")
                .build();
        Dish pasta = Dish.builder()
                .name("Паста")
                .build();
        Dish icecream = Dish.builder()
                .name("Мороженное")
                .build();
        Optional<Dish> meatballsOptional = dishRepository.findOne(
                Example.of(meatballs)
        );
        Optional<Dish> pastaOptional = dishRepository.findOne(
                Example.of(
                        pasta
                )
        );
        Optional<Dish> icecreamOptional = dishRepository.findOne(
                Example.of(
                        icecream
                )
        );
        if (meatballsOptional.isEmpty()) {
            dishRepository.save(meatballs);
        }
        if (pastaOptional.isEmpty()) {
            dishRepository.save(pasta);
        }
        if (icecreamOptional.isEmpty()) {
            dishRepository.save(icecream);
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void addLock() {
        locksRepository.save(
                FldsmdfrLock.builder()
                        .dateTime(Instant.now())
                        .type(LockStatus.UNLOCK)
                        .build()
        );
    }

    @EventListener(ApplicationStartedEvent.class)
    public void addDeliveries() {
//        deliveryRepository.save(
//                Delivery.builder()
//                        .date(LocalDate.now())
//                        .foodTime(FoodTime.BREAKFAST)
//                        .address("Кронверкский 49")
//                        .status(DeliveryStatus.NEW)
//                        .lastChangeTimestamp(Instant.now())
//                        .build()
//        );
    }

//    @EventListener(ApplicationStartedEvent.class)
//    public void testVoteRepo() throws InterruptedException {
//        Thread.sleep(1000);
//        voteRepository.save(Vote.builder()
//                        .date(LocalDate.now())
//                        .dish(dishRepository.findById(1l).orElseThrow())
//                        .foodTime(FoodTime.BREAKFAST)
//                        .user(userRepository.findById(1l).orElseThrow())
//                .build());
//        voteRepository.save(Vote.builder()
//                .date(LocalDate.now())
//                .dish(dishRepository.findById(1l).orElseThrow())
//                .foodTime(FoodTime.BREAKFAST)
//                .user(userRepository.findById(2l).orElseThrow())
//                .build());
//        voteRepository.save(Vote.builder()
//                .date(LocalDate.now())
//                .dish(dishRepository.findById(2l).orElseThrow())
//                .foodTime(FoodTime.BREAKFAST)
//                .user(userRepository.findById(3l).orElseThrow())
//                .build());
//        log.info(voteRepository.findAll().toString());
//        log.info(voteRepository.findDishesGroupedByVoteCount(LocalDate.now(),
//                FoodTime.LUNCH).toString());
//    }

}
