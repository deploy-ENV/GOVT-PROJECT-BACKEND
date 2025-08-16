package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_contractor;
import org.govt.service.UserContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class ContractorAuth {

    @Autowired
    private UserContractorService userContractorService;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/contractor")
    public Register register(@RequestBody User_contractor user) {
        return userContractorService.registerContractor(user);
    }

    @PostMapping("/login/contractor")
    public ResponseEntity<Login<User_contractor>> login(@RequestBody User_contractor user) {
        User_contractor contractor = userContractorService.findByUsername(user.getUsername());

        if (contractor != null && contractor.getPassword().equals(user.getPassword())) {
            
            String token = jwt.generateToken(contractor.getUsername());
            return ResponseEntity.ok(
                new Login<>(
                    "LoggedIn Successfully!!!",
                    token,
                    contractor
                )
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }
}
