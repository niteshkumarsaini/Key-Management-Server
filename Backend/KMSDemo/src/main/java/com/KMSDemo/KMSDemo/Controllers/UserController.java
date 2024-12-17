package com.KMSDemo.KMSDemo.Controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.KMSDemo.KMSDemo.Entities.User;
import com.KMSDemo.KMSDemo.Models.LoginRequest;
import com.KMSDemo.KMSDemo.Models.LoginResponse;
import com.KMSDemo.KMSDemo.Models.SignupRequest;
import com.KMSDemo.KMSDemo.Repositories.UserRepository;
import com.KMSDemo.KMSDemo.Services.AccountsService;
import com.KMSDemo.KMSDemo.Util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class UserController {

    private final AccountsService accountsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(AccountsService accountsService, UserRepository userRepository, JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.accountsService = accountsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        // Validate role
        if (request.getRole() == null
                || (!request.getRole().equalsIgnoreCase("ADMIN1") && !request.getRole().equalsIgnoreCase("SUPERADMIN"))
                        && !request.getRole().equalsIgnoreCase("ADMIN2")) {
            return ResponseEntity.badRequest().body("Invalid role. Role must be either 'ADMIN' or 'USER'.");
        }

        User newUser = new User();
        newUser.setRole("ROLE_" + request.getRole().toUpperCase());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole().toUpperCase()); // Store role in uppercase for consistency
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully with role: " + request.getRole());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {

            UserDetails userDetails = accountsService.loadUserByUsername(request.getUsername());
            if (userDetails != null && passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails.getUsername());
                return ResponseEntity.ok(new LoginResponse(token));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(HttpServletRequest request) {
        // Fetching token
        System.out.println("Token of header " + request.getHeader("Authorization"));
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;
        String role = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            // Fetching role as per the username
            role = userRepository.findByUsername(username).getRole();
        }

        return ResponseEntity.ok(role);

    }

    @GetMapping("/checkTokenExpired")
    public ResponseEntity<String> checkTokenExpired(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        if (jwt != null && jwtUtil.isTokenExpiredOrNot(jwt)) {
            return ResponseEntity.ok("Token is valid.");
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Token is expired.");
        }
    }

}
