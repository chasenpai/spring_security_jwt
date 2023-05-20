package com.jwtexample.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String username;
    private String password;
    private String code;

    @Builder
    public User(String username, String password, String code) {
        this.username = username;
        this.password = password;
        this.code = code;
    }
}
