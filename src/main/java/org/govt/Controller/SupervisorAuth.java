package org.govt.Controller;

import java.util.List;

import java.util.Optional;
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
                            supervisor));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }

    @GetMapping("/supervisors/getProject/{id}")
    public ResponseEntity<List<Project>> getProjectsBySupervisorId(@PathVariable String id) {
        // Check if supervisor exists
        Optional<User_Supervisor> supervisor = userSupervisorService.findById(id);
        if (supervisor.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if supervisor not found
        }

        // Fetch projects from ProjectService/Repository
        List<Project> projects = userSupervisorService.getProjectsBySupervisorId(id);
        return ResponseEntity.ok(projects);
    }

}
