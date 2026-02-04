package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_govt;
import org.govt.service.UserGovtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class GovtAuth {
    @Autowired
    private UserGovtService userGovtService;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/govt")
    public Register register(@RequestBody User_govt userGovt) {
        return userGovtService.registerGovt(userGovt);
    }

    @PostMapping("/login/govt")
    public ResponseEntity<Login<User_govt>> login(@RequestBody User_govt userGovt) {
        if (userGovtService.authenticateGovt(userGovt.getUsername(), userGovt.getPassword())) {
            User_govt govtUser = userGovtService.findByUsername(userGovt.getUsername());
            return ResponseEntity.ok(
                    new Login<>(
                            "LoggedIn Successfully!!!",
                            jwt.generateTokenWithUserId(govtUser.getUsername(), govtUser.getId()),
                            govtUser));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }

}
