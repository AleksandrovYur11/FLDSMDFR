package ru.itmo.fldsmdfr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FldsmdfrApplication {

    public static void main(String[] args) {
        SpringApplication.run(FldsmdfrApplication.class, args);
    }


}
