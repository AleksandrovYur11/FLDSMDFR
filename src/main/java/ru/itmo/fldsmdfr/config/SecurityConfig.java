package ru.itmo.fldsmdfr.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.pattern.PathPatternParser;
import ru.itmo.fldsmdfr.models.Role;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Value(value = "${server.servlet.context-path:}")
    private String p; //app context path depending on deployment; variable name is short because of very often usage

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(  "/login", "/registration", "/error", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/maintain").hasAuthority(Role.SCIENTIST.toString())
                        .requestMatchers("/delivery/**").hasAuthority(Role.DELIVERYMAN.toString())
                        .anyRequest()
                        .authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/cabinet")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"));
        return http.build();

//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers(  p+"/login", p+"/registration", p+"/error", p+"/js/**", p+"/css/**").permitAll()
//                        .requestMatchers(p+"/maintain").hasAuthority(Role.SCIENTIST.toString())
//                        .requestMatchers(p+"/delivery/**").hasAuthority(Role.DELIVERYMAN.toString())
//                        .anyRequest()
//                        .authenticated()
//                )
//                .formLogin((form) -> form
//                        .loginPage(p+"/login")
//                        .loginProcessingUrl(p+"/process_login")
//                        .defaultSuccessUrl(p+"/cabinet")
//                        .failureUrl(p+"/login?error")
//                        .permitAll()
//                )
//                .logout((logout) -> logout
//                        .logoutUrl(p+"/logout")
//                        .logoutSuccessUrl(p+"/login"));
//        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        return loggingFilter;
    }

}