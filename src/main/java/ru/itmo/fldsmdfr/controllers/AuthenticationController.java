package ru.itmo.fldsmdfr.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.fldsmdfr.dto.UserRegistrationDto;
import ru.itmo.fldsmdfr.exceptions.InvalidRoleException;
import ru.itmo.fldsmdfr.models.RefreshToken;
import ru.itmo.fldsmdfr.models.Role;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.repositories.RefreshTokenRepository;
import ru.itmo.fldsmdfr.security.exceptions.TokenRefreshException;
import ru.itmo.fldsmdfr.security.jwt.JwtUtils;
import ru.itmo.fldsmdfr.security.payload.request.LoginRequest;
import ru.itmo.fldsmdfr.security.payload.request.RefreshTokenRequest;
import ru.itmo.fldsmdfr.security.payload.response.LoginResponse;
import ru.itmo.fldsmdfr.security.payload.response.RefreshTokenResponse;
import ru.itmo.fldsmdfr.security.services.RefreshTokenService;
import ru.itmo.fldsmdfr.security.services.UserDetailsImpl;
import ru.itmo.fldsmdfr.services.RegistrationService;
import ru.itmo.fldsmdfr.util.MappingUtil;
import ru.itmo.fldsmdfr.util.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final MappingUtil mappingUtil;
    private final UserValidator userValidator;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationController(RegistrationService registrationService, AuthenticationManager authenticationManager,
                                    JwtUtils jwtUtils, RefreshTokenService refreshTokenService,
                                    MappingUtil mappingUtil, UserValidator userValidator,
                                    RefreshTokenRepository refreshTokenRepository) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.mappingUtil = mappingUtil;
        this.userValidator = userValidator;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken.getToken(), userDetails.getUser().getId(),
                userDetails.getUser().getFirstName(), userDetails.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> perfectRegistration(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                 BindingResult bindingResult) {
        userValidator.validate(userRegistrationDto, bindingResult);
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        if (!fieldErrorList.isEmpty()) {
            return new ResponseEntity<>(fieldErrorList.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
        try {
            User newUser = User.builder()
                    .firstName(userRegistrationDto.getFirstName())
                    .lastName(userRegistrationDto.getLastName())
                    .login(userRegistrationDto.getLogin())
                    .password(userRegistrationDto.getPassword())
                    .role(Role.fromString(userRegistrationDto.getRole()))
                    .address(userRegistrationDto.getAddress())
                    .build();
            System.out.println(newUser.toString());
            registrationService.register(newUser);
            return new ResponseEntity<>("Created new user", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException(userRegistrationDto.getRole(), "There is no such role");
        }
//        }  catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        System.out.println("Worked ");
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database"));
        refreshTokenService.isVerifyExpiration(token);
        User user = token.getUser();
        String newAccessToken = jwtUtils.generateJwtTokenFromLogin(user.getLogin());
        return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, refreshToken));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUser().getId();
        refreshTokenService.deleteRefreshToken(userId);
        return ResponseEntity.ok("Log out successful!");
    }


//    @Autowired
//    public AuthenticationController(RegistrationService registrationService) {
//        this.registrationService = registrationService;
//    }
//
//    @GetMapping("/registration")
//    public String getRegistrationPage(@ModelAttribute("user") User user) {
//        return "auth/registration";
//    }
//
//    @GetMapping("/login")
//    public String getLoginPage() {
//        return "auth/login";
//    }
//
//    @PostMapping("/registration")
//    public String registerUser(@ModelAttribute("user") @Valid User user) {
//        registrationService.register(UserRegistrationDto.builder().user(user).build());
//        return "redirect:/login";
//    }

}
