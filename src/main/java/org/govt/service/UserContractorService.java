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

    @Autowired 
    private  UserContractorRepository userRepository;
    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Autowired private  JwtUtil jwt;

  

    public Register registerContractor(User_contractor userContractor) {
        if (userRepository.findByUsername(userContractor.getUsername()) != null) {
            return new Register("User  already exists!!", "");
        }

        userContractor.setPassword(passwordEncoder.encode(userContractor.getPassword()));
        userRepository.save(userContractor);
        return new Register("Registered successfully!!!", jwt.generateToken(userContractor.getUsername()));
    }

    public boolean authenticateContractor(String username, String rawPassword) {
        User_contractor contractor = userRepository.findByUsername(username);
        return contractor != null && passwordEncoder.matches(rawPassword, contractor.getPassword());
    }
}
