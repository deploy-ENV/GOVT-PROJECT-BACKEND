package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_Supervisor;
import org.govt.service.UserSupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class SupervisorAuth {
    @Autowired
    private UserSupervisorService userSupervisorService;


    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/supervisor")
    public Register register(@RequestBody User_Supervisor userGovt){
        return userSupervisorService.registerSupervisor(userGovt);
    }

    @PostMapping("/login/supervisor")
    public Login login(@RequestBody User_Supervisor user_supervisor){
        if(userSupervisorService.authenticateSupervisor(user_supervisor.getUsername(), user_supervisor.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(user_supervisor.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
