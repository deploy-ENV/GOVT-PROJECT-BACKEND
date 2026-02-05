package org.govt.service;

import java.util.List;
import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.Project;
import org.govt.model.User_contractor;
import org.govt.repository.ProjectRepository;
import org.govt.repository.UserContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserContractorService {

    @Autowired
    private UserContractorRepository userRepository;
    @Autowired
    private ProjectRepository projectRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtUtil jwt;

    public Register registerContractor(User_contractor userContractor) {
        if (findByUsername(userContractor.getUsername()) != null) {
            return new Register("User  already exists!!", jwt.generateToken(userContractor.getUsername()),
                    findByUsername(userContractor.getUsername()));
        }

        userContractor.setPassword(passwordEncoder.encode(userContractor.getPassword()));
        userRepository.save(userContractor);
        return new Register("Registered successfully!!!", jwt.generateToken(userContractor.getUsername()),
                findByUsername(userContractor.getUsername()));
    }

    public User_contractor findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean authenticateContractor(String username, String rawPassword) {
        User_contractor contractor = userRepository.findByUsername(username);
        return contractor != null && passwordEncoder.matches(rawPassword, contractor.getPassword());
    }

    public User_contractor getById(String contractorId) {
        return userRepository.findById(contractorId).get();
    }

    public void updateUserContractor(User_contractor userContractor) {
        userRepository.save(userContractor);
    }

    public List<Project> getProject(String id) {
        return projectRepo.findByAssignedContractorId(id);
    }

}
