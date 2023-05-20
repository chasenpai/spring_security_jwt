package com.exampleotp.service;

import com.exampleotp.entity.Otp;
import com.exampleotp.entity.User;
import com.exampleotp.repository.OtpRepository;
import com.exampleotp.repository.UserRepository;
import com.exampleotp.request.OtpRequest;
import com.exampleotp.request.UserRequest;
import com.exampleotp.util.GenerateCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserOtpService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    /**
     * 사용자 저장
     */
    @Transactional
    public void saveUser(UserRequest requestParam){
        requestParam.setPassword(passwordEncoder.encode(requestParam.getPassword()));
        userRepository.save(new User((requestParam)));
    }

    /**
     * 사용자 인증
     */
    @Transactional
    public void authUser(UserRequest requestParam){

        User user = userRepository.findByUsername(requestParam.getUsername())
                .orElseThrow(() -> new BadCredentialsException("bad credentials.."));

        if(passwordEncoder.matches(requestParam.getPassword(), user.getPassword())){
            renewOtp(user); // 비밀번호 일치하면 새로운 OTP 생성
        }else{
            throw new BadCredentialsException("bad credentials..");
        }

    }

    /**
     * OTP 생성
     */
    @Transactional
    private void renewOtp(User user){
        String code = GenerateCodeUtil.generateCode(); //OTP 랜덤코드 생성
        Optional<Otp> requestOtp = otpRepository.findByUsername(user.getUsername());

        if(requestOtp.isPresent()){
            Otp otp = requestOtp.get();
            otp.updateOtpCode(code);
        }else{
            otpRepository.save(new Otp(user.getUsername(), code));
        }

    }

    /**
     * OTP 검증
     */
    public void validateOtp(OtpRequest requestParam){

        Otp otp = otpRepository.findByUsername(requestParam.getUsername())
                .orElseThrow(() -> new BadCredentialsException("bad credentials.."));

        if(!requestParam.getCode().equals(otp.getCode())){
            throw new BadCredentialsException("bad credentials..");
        }

    }

}
