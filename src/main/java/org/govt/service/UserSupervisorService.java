package org.govt.service;

import java.util.List;

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
    private final UserSupervisorRepository userSupervisorRepository;
    private final PasswordEncoder password;

    @Autowired
    private JwtUtil jwt=new JwtUtil();


public List<User_Supervisor> getSupervisorsByZone(String zone) {
    return userSupervisorRepository.findByZone(zone);
}

    public UserSupervisorService(UserSupervisorRepository user){
        this.userSupervisorRepository =user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerSupervisor(User_Supervisor user_supervisor){
        if(userSupervisorRepository.findByUsername(user_supervisor.getUsername())!=null){
            return new Register("User Already Exists!!!!","");
        }

        user_supervisor.setPassword(password.encode(user_supervisor.getPassword()));
        userSupervisorRepository.save(user_supervisor);
        return new Register("Registered Successfully!!!", jwt.generateToken(user_supervisor.getUsername()));
    }

    public boolean authenticateSupervisor(String username,String pass){
        User_Supervisor user1= userSupervisorRepository.findByUsername(username);
        return user1!=null && password.matches(pass,user1.getPassword());
    }

}
