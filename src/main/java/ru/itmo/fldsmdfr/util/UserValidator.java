package ru.itmo.fldsmdfr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.fldsmdfr.models.User;
@Component
public class UserValidator implements Validator {

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserValidator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userDetailsService.loadUserByUsername(user.getLogin());
        } catch (UsernameNotFoundException ignored){
            return;
        }
    }
}
