package com.exampleotp.util;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenerateCodeUtil {

    /**
     * OTP 랜덤 값 생성 유틸
     */
    public static String generateCode() {

        try{
            SecureRandom random = SecureRandom.getInstanceStrong(); //임의의 정수를 생성
            int tmpCode = random.nextInt(9000) + 1000; //0 ~ 8999 사이 생성하고 1000 더해서 1000 ~ 9999 4자리 랜덤코드 얻음
            return String.valueOf(tmpCode);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("generating random code error..");
        }

    }

}
