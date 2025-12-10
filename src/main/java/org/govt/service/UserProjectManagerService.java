package org.govt.service;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.User_ProjectManager;
import org.govt.repository.UserProjectManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProjectManagerService {
    @Autowired
    private  UserProjectManagerRepository userProjectManagerRepository;
    @Autowired
    private  PasswordEncoder password=new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwt;



    public Register registerProjectManager(User_ProjectManager user_projectManager){
        if(findByUsername(user_projectManager.getUsername())!=null){
            return new Register("User Already Exists!!!!", jwt.generateToken(user_projectManager.getUsername()),findByUsername(user_projectManager.getUsername()));
        }
        user_projectManager.setPassword(password.encode(user_projectManager.getPassword()));
        userProjectManagerRepository.save(user_projectManager);
        return new Register("Registered Successfully!!!", jwt.generateToken(user_projectManager.getUsername()),findByUsername(user_projectManager.getUsername()));
    }
public User_ProjectManager findByUsername(String username) {
        return userProjectManagerRepository.findByUsername(username);
    }
    public boolean authenticateProjectManager(String username,String pass){
        User_ProjectManager user1= userProjectManagerRepository.findByUsername(username);
        return user1!=null && password.matches(pass,user1.getPassword());
    }

}
