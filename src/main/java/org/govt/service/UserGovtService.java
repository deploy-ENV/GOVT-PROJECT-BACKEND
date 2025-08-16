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
    @Autowired
    private UserGovtRepository userGovtRepository;
    @Autowired
    private PasswordEncoder password;

    @Autowired
    private JwtUtil jwt;

    public UserGovtService(UserGovtRepository user){
        this.userGovtRepository =user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerGovt(User_govt userGovt){
        if(findByUsername(userGovt.getUsername())!=null){
            return new Register("User Already Exists!!!!","");
        }
        userGovt.setPassword(userGovt.getPassword());
        userGovtRepository.save(userGovt);
        return new Register("Registered Successfully!!!", jwt.generateToken(userGovt.getUsername()));
    }
     public User_govt findByUsername(String username) {
        return userGovtRepository.findByUsername(username);
    }

    public boolean authenticateGovt(String username,String pass){
        User_govt govt= userGovtRepository.findByUsername(username);
        return govt!=null && password.matches(pass,govt.getPassword());
    }

}
