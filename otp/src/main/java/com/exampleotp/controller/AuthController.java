package com.exampleotp.controller;

import com.exampleotp.request.OtpRequest;
import com.exampleotp.request.UserRequest;
import com.exampleotp.service.UserOtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/otp")
public class AuthController {

    private final UserOtpService userOtpService;

    @PostMapping("/v1/user")
    public ResponseEntity<Void> saveUser(@RequestBody UserRequest requestParam){
        userOtpService.saveUser(requestParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/v1/user/authenticate")
    public ResponseEntity<Void> authUser(@RequestBody UserRequest requestParam){
        userOtpService.authUser(requestParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/v1/otp")
    public ResponseEntity<Boolean> validateOtp(@RequestBody OtpRequest requestParam){
        return ResponseEntity.status(HttpStatus.OK).body(userOtpService.validateOtp(requestParam));
    }


}
