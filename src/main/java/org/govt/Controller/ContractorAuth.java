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
    private UserContractorService user;

    @Autowired
    private JwtUtil jwt= new JwtUtil();

    public ContractorAuth(UserContractorService user){
        this.user=user;
    }

    @PostMapping("/register/contractor")
    public Register register(@RequestBody User_contractor user1){
        return user.registerContractor(user1.getName(),user1.getUsername(),user1.getPassword(),user1.getDOB(),user1.getPhone(),user1.getEmail(),user1.getGst_number(),user1.getAddress(),user1.getPincode());
    }

    @PostMapping("/login/contractor")
    public Login login(@RequestBody User_contractor user1){
        if(user.authenticateContractor(user1.getUsername(),user1.getPassword())){
            return new Login("LoggedIn Successfully!!!",jwt.generateToken(user1.getUsername()));
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
