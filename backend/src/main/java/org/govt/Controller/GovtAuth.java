package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_govt;
import org.govt.service.UserGovtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth")
public class GovtAuth {
    @Autowired
    private UserGovtService userGovtService;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/govt")
    public Register register(@RequestBody User_govt userGovt){
        return userGovtService.registerGovt(userGovt);
    }

    @PostMapping("/login/govt")
    public Login login(@RequestBody User_govt userGovt){
        if(userGovtService.authenticateGovt(userGovt.getUsername(), userGovt.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(userGovt.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
