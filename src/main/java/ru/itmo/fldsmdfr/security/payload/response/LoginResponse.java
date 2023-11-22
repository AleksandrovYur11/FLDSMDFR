package ru.itmo.fldsmdfr.security.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String firstName;
    private String login;

    public LoginResponse(String accessToken, String refreshToken, Long id, String firstName, String login) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.firstName = firstName;
        this.login = login;
    }
}
