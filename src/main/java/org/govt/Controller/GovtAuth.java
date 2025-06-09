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
    private UserGovtService user;

    public GovtAuth(UserGovtService user){
        this.user=user;
    }

    @Autowired
    private JwtUtil jwt= new JwtUtil();

    @PostMapping("/register/govt")
    public Register register(@RequestBody User_govt user1){
        return user.registerGovt(user1.getName(),user1.getUsername(),user1.getPassword(),user1.getDOB(),user1.getEmail(),user1.getGovt_Id(),user1.getGovt_department(),user1.getPincode());
    }

    @PostMapping("/login/govt")
    public Login login(@RequestBody User_govt user1){
        if(user.authenticateGovt(user1.getUsername(),user1.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(user1.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
