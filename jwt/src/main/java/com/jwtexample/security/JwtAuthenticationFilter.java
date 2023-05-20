package com.jwtexample.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;

    /**
     * JWT 인증 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //JWT 토큰 추출
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8)); //서명 검증을 위한 key

        /**
         * Jwts.parserBuilder - JWT 파싱하기 위한 빌더 객체
         * setSigningKey - 서명에 사용할 키를 설정
         * parseClaimsJws - 파서를 사용하여 JWT를 검증
         * getBody - 검증된 JWT body 부분에 해당하는 클레임을 추출
         */
        Claims claims = Jwts.parserBuilder()//JWT 토큰의 검증과 클레임을 추출한다
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = String.valueOf(claims.get("username"));
        System.out.println("username = " + username);
        String role = String.valueOf(claims.get("role"));
        System.out.println("role = " + role);

        //추출한 정보로 SecurityContext 에 추가할 Authentication 객체를 생성
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        UserAuthentication authUser = new UserAuthentication(username, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authUser);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login"); //login 경로는 필터 제외
    }
}
