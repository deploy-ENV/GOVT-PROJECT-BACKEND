package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_Supplier;
import org.govt.service.UserSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class SupplierAuth {

    @Autowired
    private UserSupplierService userSupplierService;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/supplier")
    public Register register(@RequestBody User_Supplier userSupplier) {
        return userSupplierService.registerSupplier(userSupplier);
    }

    @PostMapping("/login/supplier")
    public Login login(@RequestBody User_Supplier userSupplier){
        if(userSupplierService.authenticateSupplier(userSupplier.getUsername(), userSupplier.getPassword())){
            String token = jwt.generateToken("himanshu3");
            return new Login("LoggedIn Successfully!!!",token);
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
