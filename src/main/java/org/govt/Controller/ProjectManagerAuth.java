package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_ProjectManager;
import org.govt.service.UserProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class ProjectManagerAuth {

    @Autowired
    private UserProjectManagerService userProjectManagerService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/projectmanager")
    public Register register(@RequestBody User_ProjectManager user) {
        return userProjectManagerService.registerProjectManager(user);
    }

    @PostMapping("/login/projectmanager")
    public ResponseEntity<Login<User_ProjectManager>> login(@RequestBody User_ProjectManager userProjectManager) {
        User_ProjectManager manager = userProjectManagerService.findByUsername(userProjectManager.getUsername());

        if (manager != null && passwordEncoder.matches(userProjectManager.getPassword(), manager.getPassword())) {
            String token = jwt.generateTokenWithUserId(manager.getUsername(), manager.getId());
            return ResponseEntity.ok(
                    new Login<>(
                            "LoggedIn Successfully!!!",
                            token,
                            manager));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }
}
