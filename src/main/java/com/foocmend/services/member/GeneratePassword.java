package com.foocmend.services.member;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GeneratePassword {

    Random rnd = new Random();
    StringBuffer sb = new StringBuffer();

    public String GeneratePw() {

        for(int i = 0; i < 10; i++) {
            if(rnd.nextBoolean()) {
                sb.append((char)((int)(rnd.nextInt(26))+97));
            } else {
                sb.append((rnd.nextInt(10)));
            }
        }
        return sb.toString();
    }

}
