package org.govt.Controller;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Login;
import org.govt.login_message.Register;
import org.govt.model.User_Supplier;
import org.govt.service.UserSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class SupplierAuth {

    @Autowired
    private UserSupplierService userSupplierService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register/supplier")
    public Register register(@RequestBody User_Supplier userSupplier) {
        return userSupplierService.registerSupplier(userSupplier);
    }

    @PostMapping("/login/supplier")
    public ResponseEntity<Login<User_Supplier>> login(@RequestBody User_Supplier userSupplier) {
        User_Supplier supplier = userSupplierService.findByUsername(userSupplier.getUsername());

        if (supplier != null && passwordEncoder.matches(userSupplier.getPassword(), supplier.getPassword())) {
          
            String token = jwt.generateToken(supplier.getUsername());
            return ResponseEntity.ok(
                new Login<>(
                    "LoggedIn Successfully!!!",
                    token,
                    supplier
                )
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Login<>("Invalid Credentials!!!", "", null));
        }
    }
}
