package com.jwtexample.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authManager;

    @Value("${jwt.signing.key}")
    private String signingKey;

    /**
     * 사용자 인증 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if(code == null){ //사용자 인증
            Authentication auth = new UserAuthentication(username, password);
            authManager.authenticate(auth);
        }else{ //Otp 인증 후 Jwt 생성
            Authentication auth = new OtpAuthentication(username, code);
            auth = authManager.authenticate(auth);

            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8)); //signing key 값을 사용하여 서명

            /**
             * Jwts.builder - JWT를 구축하기 위한 빌더 객체
             * setClaims - JWT의 클레임에 식별 정보 등을 저장
             * signWith - JWT를 서명한다
             * compact - 최종적으로 JWT를 문자열 형태로 변환
             */
            String jwt = Jwts.builder() //Jwt 를 구축하고 사용자 이름과 권한을 클레임으로 설정하고 토큰 서명은 키를 이용
                    .setClaims(Map.of("username", username, "role", "guest"))
                    .signWith(key)
                    .compact();

            response.setHeader("Authorization", jwt); //Authorization 헤더에 jwt 추가
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login"); //login 경로만 필터 적용
    }

}
