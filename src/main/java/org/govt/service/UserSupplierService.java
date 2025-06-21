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
    @Autowired
    private UserSupplierRepository userSupplierRepository;
    @Autowired
    private  PasswordEncoder password;

    @Autowired
    JwtUtil jwt =new JwtUtil();

    public Register registerSupplier(User_Supplier userSupplier){
        if(userSupplierRepository.findByUsername(userSupplier.getUsername())!=null){
            return new Register("User Already Exists!!","");
        }
        userSupplier.setPassword(password.encode(userSupplier.getPassword()));
        userSupplierRepository.save(userSupplier);
        return new Register("User Registered!!!", jwt.generateToken(userSupplier.getUsername()));
    }

    public boolean authenticateSupplier(String username,String password1){
        BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
        User_Supplier supplier= userSupplierRepository.findByUsername(username);
        return supplier!=null && password.matches(password1,supplier.getPassword());
    }
}
