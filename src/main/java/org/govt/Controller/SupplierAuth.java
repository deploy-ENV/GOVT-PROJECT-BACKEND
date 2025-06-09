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
    private UserSupplierService user;

    @Autowired
    private JwtUtil jwt=new JwtUtil();

    public SupplierAuth(UserSupplierService user){
        this.user=user;
    }

    @PostMapping("/register/supplier")
    public Register register(@RequestBody User_Supplier user1) {
        return user.registerSupplier(
                user1.getName(), user1.getUsername(), user1.getPassword(), user1.getDOB(),
                user1.getPhone(), user1.getEmail(), user1.getGst_number(), user1.getAddress(),user1.getPincode()
        );
    }

    @PostMapping("/login/supplier")
    public Login login(@RequestBody User_Supplier user1){
        if(user.authenticateSupplier(user1.getUsername(),user1.getPassword())){
            String token = jwt.generateToken("himanshu3");
            return new Login("LoggedIn Successfully!!!",token);
        }
        else{
            return new Login("Invalid Credentials!!!","");
        }
    }
}
