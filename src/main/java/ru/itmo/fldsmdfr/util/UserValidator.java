package ru.itmo.fldsmdfr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.fldsmdfr.dto.UserRegistrationDto;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.UserRepository;

@Component
public class UserValidator implements Validator {

    private final MappingUtil mappingUtil;
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(MappingUtil mappingUtil, UserRepository userRepository) {
        this.mappingUtil = mappingUtil;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) target;
        User user = mappingUtil.convertToUser(userRegistrationDto);


        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            errors.rejectValue("login", "409", "User with email " + user.getLogin() +
                    " already exists");

        }

//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            Map<String, String> errorMap = new HashMap<>();
//            errorMap.put("email", "This email is already taken");
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                String errorMessage = objectMapper.writeValueAsString(errorMap);
////                objectMapper.writeValue("email", errorMessage)
//                errors.rejectValue("email", "", errorMessage);
//            } catch (JsonProcessingException e) {
//                // Обработка ошибки при преобразовании в JSON
//                e.printStackTrace();
//                // Можно также добавить стандартное сообщение об ошибке, если не удается преобразовать в JSON
//                errors.rejectValue("email", "", "This email is already taken");
//            }
//        }
    }
}
