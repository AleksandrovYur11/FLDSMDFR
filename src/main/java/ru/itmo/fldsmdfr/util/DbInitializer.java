package ru.itmo.fldsmdfr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itmo.fldsmdfr.models.*;
import ru.itmo.fldsmdfr.repositories.DeliveryRepository;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.FldsmdfrLocksRepository;
import ru.itmo.fldsmdfr.repositories.UserRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class DbInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DishRepository dishRepository;
    private final FldsmdfrLocksRepository locksRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DbInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, DishRepository dishRepository, FldsmdfrLocksRepository fldsmdfrLocksRepository, DeliveryRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dishRepository = dishRepository;
        this.locksRepository = fldsmdfrLocksRepository;
        this.deliveryRepository = deliveryRepository;
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
                    .firstName("житель")
                    .lastName("простой")
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
        deliveryRepository.save(
                Delivery.builder()
                        .date(LocalDate.now())
                        .foodTime(FoodTime.BREAKFAST)
                        .address("Кронверкский 49")
                        .status(DeliveryStatus.NEW)
                        .lastChangeTimestamp(Instant.now())
                        .build()
        );
    }

}
