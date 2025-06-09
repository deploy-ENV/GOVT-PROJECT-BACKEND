package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_govt;
import org.govt.service.UserSupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class SupervisorAuth {
    private UserSupervisorService user;

    public SupervisorAuth(UserSupervisorService user){
        this.user=user;
    }

    @Autowired
    private JwtUtil jwt= new JwtUtil();

    @PostMapping("/register/supervisor")
    public Register register(@RequestBody User_govt user1){
        return user.registerSupervisor(user1.getName(),user1.getUsername(),user1.getPassword(),user1.getDOB(),user1.getEmail(),user1.getGovt_Id(),user1.getGovt_department(),user1.getPincode());
    }

    @PostMapping("/login/supervisor")
    public Login login(@RequestBody User_govt user1){
        if(user.authenticateSupervisor(user1.getUsername(),user1.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(user1.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
