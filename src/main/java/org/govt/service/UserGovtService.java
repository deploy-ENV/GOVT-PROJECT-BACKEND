package org.govt.service;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.User_govt;
import org.govt.repository.UserGovtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserGovtService {
    private final UserGovtRepository user;
    private final PasswordEncoder password;

    @Autowired
    private JwtUtil jwt=new JwtUtil();

    public UserGovtService(UserGovtRepository user){
        this.user=user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerGovt(String name, String username, String password1, String DOB, String email, String govt_Id, String govt_department,String pincode){
        if(user.findByUsername(username)!=null){
            return new Register("User Already Exists!!!!","");
        }
        User_govt userGovt=new User_govt(name,username,password1,DOB,email,govt_Id,govt_department,pincode);
        String hash=password.encode(password1);
        userGovt.setPassword(hash);
        user.save(userGovt);
        return new Register("Registered Successfully!!!", jwt.generateToken(username));
    }

    public boolean authenticateGovt(String username,String pass){
        User_govt govt=user.findByUsername(username);
        return govt!=null && password.matches(pass,govt.getPassword());
    }

}
