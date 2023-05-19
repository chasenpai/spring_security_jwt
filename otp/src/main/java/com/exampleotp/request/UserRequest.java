package com.exampleotp.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    private String username;
    private String password;

    @Builder
    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
