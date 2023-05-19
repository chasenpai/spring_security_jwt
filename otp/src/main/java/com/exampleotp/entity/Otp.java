package com.exampleotp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Otp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String code;

    public Otp(String username, String code) {
        this.username = username;
        this.code = code;
    }

    public void updateOtpCode(String code){
        this.code = code;
    }

}
