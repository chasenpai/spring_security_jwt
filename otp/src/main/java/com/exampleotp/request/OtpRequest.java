package com.exampleotp.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpRequest {

    private String username;
    private String code;

    @Builder
    public OtpRequest(String username, String code) {
        this.username = username;
        this.code = code;
    }

}
