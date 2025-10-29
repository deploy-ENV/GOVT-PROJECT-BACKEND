package org.govt.Controller;

import java.util.List;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.Project;
import org.govt.model.User_Supervisor;
import org.govt.service.UserSupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class SupervisorAuth {

    @Autowired
    private UserSupervisorService userSupervisorService;
private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/supervisor")
    public Register register(@RequestBody User_Supervisor userSupervisor) {
        return userSupervisorService.registerSupervisor(userSupervisor);
    }

    @PostMapping("/login/supervisor")
    public ResponseEntity<Login<User_Supervisor>> login(@RequestBody User_Supervisor userSupervisor) {
        User_Supervisor supervisor = userSupervisorService.findByUsername(userSupervisor.getUsername());

        if (supervisor != null && passwordEncoder.matches(userSupervisor.getPassword(), supervisor.getPassword())) {
           
            String token = jwt.generateToken(supervisor.getUsername());
            return ResponseEntity.ok(
                new Login<>(
                    "LoggedIn Successfully!!!",
                    token,
                    supervisor
                )
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }
    @GetMapping("/supervisors/getProject/{id}")
    public List<Project> getProjectsBySupervisorId(@PathVariable String id) {
        User_Supervisor supervisor = userSupervisorService.findByUsername(id);
        if (supervisor != null) {
            return userSupervisorService.getProjectsBySupervisorId(id);
        }
        return List.of(); // Return an empty list if supervisor not found
    }
    
    
}
