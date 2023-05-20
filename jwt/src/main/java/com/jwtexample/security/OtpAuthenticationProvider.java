package com.jwtexample.security;

import com.jwtexample.module.AuthenticationServerProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy serverProxy;

    /**
     * Otp 인증 처리
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String code = (String) authentication.getCredentials();

        boolean result = serverProxy.sendOtp(username, code);

        if(result){
            return new OtpAuthentication(username, code);
        }else{
            throw new BadCredentialsException("bad credentials...");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
