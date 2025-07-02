package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_contractor;
import org.govt.service.UserContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class ContractorAuth {
    @Autowired private UserContractorService userContractorService;

    @Autowired
    private JwtUtil jwt;



    @PostMapping("/register/contractor")
    public Register register(@RequestBody User_contractor user){
        return userContractorService.registerContractor(user);
    }

    @PostMapping("/login/contractor")
    public Login login(@RequestBody User_contractor user1){
        if(userContractorService.authenticateContractor(user1.getUsername(),user1.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(user1.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }

}
