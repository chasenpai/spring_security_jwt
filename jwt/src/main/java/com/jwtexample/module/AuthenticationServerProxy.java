package com.jwtexample.module;

import com.jwtexample.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationServerProxy {

    private final WebClient webClient;

    public boolean sendAuth(String username, String password){

        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        return Boolean.TRUE.equals(webClient.post()
                .uri("/v1/user/authenticate")
                .body(Mono.just(user), User.class)
                .exchangeToMono(
                        response -> response.statusCode().is2xxSuccessful() ?
                                Mono.just(true) : Mono.just(false))
                .block());
    }

    public boolean sendOtp(String username, String code){

        User user = User.builder()
                .username(username)
                .code(code)
                .build();

        return Boolean.TRUE.equals(webClient.post()
                .uri("/v1/otp")
                .body(Mono.just(user), User.class)
                .exchangeToMono(
                        response -> response.statusCode().is2xxSuccessful() ?
                                Mono.just(true) : Mono.just(false))
                .block());
    }

}
