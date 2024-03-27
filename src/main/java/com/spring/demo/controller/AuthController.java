package com.spring.demo.controller;

import com.spring.demo.dto.request.SignInForm;
import com.spring.demo.dto.request.SignUpForm;
import com.spring.demo.dto.response.JwtResponse;
import com.spring.demo.dto.response.ResponseMessage;
import com.spring.demo.model.Role;
import com.spring.demo.model.RoleName;
import com.spring.demo.model.User;
import com.spring.demo.security.jwt.JwtProvider;
import com.spring.demo.security.userprincal.UserPrinciple;
import com.spring.demo.service.implement.RoleServiceImpl;
import com.spring.demo.service.implement.UserServiceImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userService.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("username existed"), HttpStatus.NOT_IMPLEMENTED);
        }
        if (userService.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("email existed"), HttpStatus.NOT_IMPLEMENTED);
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByRole(RoleName.ADMIN).orElseThrow(() -> new RuntimeException("role not found"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByRole(RoleName.PM).orElseThrow(() -> new RuntimeException("role not found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByRole(RoleName.USER).orElseThrow(() -> new RuntimeException("role not found"));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("add user successfully"), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAuthorities()));
    }
}
