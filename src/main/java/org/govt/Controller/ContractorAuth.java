package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_contractor;
import org.govt.service.UserContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class ContractorAuth {

    @Autowired
    private UserContractorService userContractorService;

    @Autowired
    private JwtUtil jwt;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register/contractor")
    public Register register(@RequestBody User_contractor user) {
        return userContractorService.registerContractor(user);
    }

    @PostMapping("/login/contractor")
    public ResponseEntity<Login<User_contractor>> login(@RequestBody User_contractor user) {
        User_contractor contractor = userContractorService.findByUsername(user.getUsername());

        if (contractor != null && passwordEncoder.matches(user.getPassword(), contractor.getPassword())) {

            String token = jwt.generateTokenWithUserId(contractor.getUsername(), contractor.getId());
            return ResponseEntity.ok(
                    new Login<>(
                            "LoggedIn Successfully!!!",
                            token,
                            contractor));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }

}
