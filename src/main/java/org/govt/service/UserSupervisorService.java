package org.govt.service;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.User_Supervisor;
import org.govt.repository.UserSupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSupervisorService {
    private final UserSupervisorRepository user;
    private final PasswordEncoder password;

    @Autowired
    private JwtUtil jwt=new JwtUtil();

    public UserSupervisorService(UserSupervisorRepository user){
        this.user=user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerSupervisor(String name, String username, String password1, String DOB, String email, String govt_Id, String govt_department, String pincode){
        if(user.findByUsername(username)!=null){
            return new Register("User Already Exists!!!!","");
        }
        User_Supervisor user_supervisor=new User_Supervisor(name,username,password1,DOB,email,govt_Id,govt_department,pincode);
        String hash=password.encode(password1);
        user_supervisor.setPassword(hash);
        user.save(user_supervisor);
        return new Register("Registered Successfully!!!", jwt.generateToken(username));
    }

    public boolean authenticateSupervisor(String username,String pass){
        User_Supervisor user1=user.findByUsername(username);
        return user1!=null && password.matches(pass,user1.getPassword());
    }

}
