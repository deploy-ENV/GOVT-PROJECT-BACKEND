package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_ProjectManager;
import org.govt.service.UserProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth")
public class ProjectManagerAuth {
    @Autowired
    private UserProjectManagerService userProjectManagerService;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/projectmanager")
    public Register register(@RequestBody User_ProjectManager user1){
        return userProjectManagerService.registerProjectManager(user1);
    }

    @PostMapping("/login/projectmanager")
    public Login login(@RequestBody User_ProjectManager user_projectManager){
        if(userProjectManagerService.authenticateProjectManager(user_projectManager.getUsername(), user_projectManager.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(user_projectManager.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
