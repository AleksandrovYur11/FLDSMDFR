package ru.itmo.fldsmdfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.UserRepository;

import java.util.Optional;

@SpringBootApplication
public class FldsmdfrApplication {

    public static void main(String[] args) {
        SpringApplication.run(FldsmdfrApplication.class, args);
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FldsmdfrApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void afterStartup() {
        Optional<User> citizenUserOptional = userRepository.findByLogin("citizen");
        Optional<User> deliverymanUserOptional = userRepository.findByLogin("deliveryman");
        Optional<User> scientistUserOptional = userRepository.findByLogin("scientist");

        if(citizenUserOptional.isEmpty()) {
            userRepository.save(User.builder()
                    .login("citizen")
                    .password(passwordEncoder.encode("citizen"))
                    .role(Role.CITIZEN)
                    .firstName("житель")
                    .lastName("простой")
                    .build());
        }
        if(deliverymanUserOptional.isEmpty()) {
            userRepository.save(User.builder()
                    .login("deliveryman")
                    .password(passwordEncoder.encode("deliveryman"))
                    .role(Role.DELIVERYMAN)
                    .firstName("доставщик")
                    .lastName("работящий")
                    .build());
        }
        if(scientistUserOptional.isEmpty()) {
            userRepository.save(User.builder()
                    .login("scientist")
                    .password(passwordEncoder.encode("scientist"))
                    .role(Role.SCIENTIST)
                    .firstName("ученый")
                    .lastName("гениальный")
                    .build());
        }
    }

}
