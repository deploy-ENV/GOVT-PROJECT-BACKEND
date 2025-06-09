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
    private final UserProjectManagerRepository user;
    private final PasswordEncoder password;

    @Autowired
    private JwtUtil jwt=new JwtUtil();

    public UserProjectManagerService(UserProjectManagerRepository user){
        this.user=user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerProjectManager(String name, String username, String password1, String DOB, String email, String govt_Id, String govt_department, String pincode){
        if(user.findByUsername(username)!=null){
            return new Register("User Already Exists!!!!","");
        }
        User_ProjectManager user_projectManager=new User_ProjectManager(name,username,password1,DOB,email,govt_Id,govt_department,pincode);
        String hash=password.encode(password1);
        user_projectManager.setPassword(hash);
        user.save(user_projectManager);
        return new Register("Registered Successfully!!!", jwt.generateToken(username));
    }

    public boolean authenticateProjectManager(String username,String pass){
        User_ProjectManager user1=user.findByUsername(username);
        return user1!=null && password.matches(pass,user1.getPassword());
    }

}
