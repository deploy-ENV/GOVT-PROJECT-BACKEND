package org.govt.service;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.User_Supplier;
import org.govt.repository.UserSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSupplierService {
    private final UserSupplierRepository user;
    private final PasswordEncoder password;

    @Autowired
    JwtUtil jwt =new JwtUtil();

    public UserSupplierService(UserSupplierRepository user){
        this.user=user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerSupplier(String name, String username, String password1, String DOB, long phone, String email, String gst_number, String address,String pincode){
        if(user.findByUsername(username)!=null){
            return new Register("User Already Exists!!","");
        }
        User_Supplier Supplier=new User_Supplier(name,username,password1,DOB,phone,email,gst_number,address,pincode);
        String hash=password.encode(password1);
        Supplier.setPassword(hash);
        user.save(Supplier);
        return new Register("User Registered!!!", jwt.generateToken(username));
    }

    public boolean authenticateSupplier(String username,String password1){
        BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
        User_Supplier supplier=user.findByUsername(username);
        return supplier!=null && password.matches(password1,supplier.getPassword());
    }
}
