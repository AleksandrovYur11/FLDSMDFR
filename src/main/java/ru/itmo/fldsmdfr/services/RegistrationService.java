package ru.itmo.fldsmdfr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User newUser) {
        String password = newUser.getPassword();
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
    }
}
