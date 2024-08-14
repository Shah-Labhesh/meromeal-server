package com.labhesh.MeroMeal.utils;

public class OtpUtils {
    

    public static String generateOtp() {
        int randomPin   =(int) (Math.random()*90000)+10000;
        return String.valueOf(randomPin);
    }
}
