package ru.itmo.fldsmdfr.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRegistrationDto {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String role;

}
