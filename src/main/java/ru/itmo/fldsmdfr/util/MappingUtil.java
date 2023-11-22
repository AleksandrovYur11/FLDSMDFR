package ru.itmo.fldsmdfr.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.dto.UserRegistrationDto;
import ru.itmo.fldsmdfr.models.User;

@Service
public class MappingUtil {
    private final ModelMapper modelMapper;

    @Autowired
    public MappingUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public User convertToUser(UserRegistrationDto userRegistrationDto) {
        return modelMapper.map(userRegistrationDto, User.class);
    }

}
