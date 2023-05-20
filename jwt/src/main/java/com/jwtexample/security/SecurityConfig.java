package com.jwtexample.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Arrays.asList(userAuthenticationProvider, otpAuthenticationProvider));
    }

    @Bean
    public InitialAuthenticationFilter initialAuthenticationFilter(){ //AuthenticationManager 에 대한 순환참조가 일어나서 빈으로 등록
        return new InitialAuthenticationFilter(authenticationManager());
    }

    /**
     * 시큐리티 6.1 부터 변경된 filter chain 구성법
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(httpBasic -> {
            httpBasic.disable();
        });
        http.csrf(csrf -> {
            csrf.disable();
        });
        http.addFilterAt(initialAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
        http.authorizeHttpRequests(request -> {
            request.anyRequest().authenticated();
        });

        return http.build();
    }

}
