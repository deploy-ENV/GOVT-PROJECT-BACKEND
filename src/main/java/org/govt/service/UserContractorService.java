package org.govt.service;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.User_contractor;
import org.govt.repository.UserContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserContractorService {
    private final UserContractorRepository user;
    private final PasswordEncoder password;

    @Autowired
    JwtUtil jwt= new JwtUtil();

    public UserContractorService(UserContractorRepository user){
        this.user=user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerContractor(String name, String username, String password1, String DOB, long phone, String email, String gst_number, String address,String pincode){
        if(user.findByUsername(username)!=null){
            return new Register("User already Exist!!","");
        }
        User_contractor Contractor = new User_contractor(name,username,password1,DOB,phone,email,gst_number,address,pincode);
        String hash=password.encode(password1);
        Contractor.setPassword(hash);
        user.save(Contractor);
        return new Register("Registered Successfully!!!",jwt.generateToken(username));
    }

    public boolean authenticateContractor(String username,String password1){
        User_contractor contractor= user.findByUsername(username);
        return contractor!=null && password.matches(password1,contractor.getPassword());
    }
}
