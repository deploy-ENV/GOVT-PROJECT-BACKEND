package org.govt.login_message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Register {
    String message;
    String token;
    public Register(String message,String token){
        this.message=message;
        this.token=token;
    }
}
