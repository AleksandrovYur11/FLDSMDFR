package ru.itmo.fldsmdfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.UserRepository;

import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class FldsmdfrApplication {

    public static void main(String[] args) {
        SpringApplication.run(FldsmdfrApplication.class, args);
    }

}
