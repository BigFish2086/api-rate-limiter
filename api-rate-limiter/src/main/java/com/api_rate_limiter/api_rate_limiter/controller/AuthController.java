package com.api_rate_limiter.api_rate_limiter.controller;

import jakarta.validation.Valid;
import com.api_rate_limiter.api_rate_limiter.dto.UserDto;
import com.api_rate_limiter.api_rate_limiter.entity.User;
import com.api_rate_limiter.api_rate_limiter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api_rate_limiter.api_rate_limiter.dto.UserDto;
import com.api_rate_limiter.api_rate_limiter.dto.LoginDto;
import com.api_rate_limiter.api_rate_limiter.entity.User;
import com.api_rate_limiter.api_rate_limiter.response.LoginResponse;
import com.api_rate_limiter.api_rate_limiter.service.JwtService;
import com.api_rate_limiter.api_rate_limiter.service.UserService;
import com.api_rate_limiter.api_rate_limiter.service.UserServiceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private UserService userService;
    private final JwtService jwtService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AuthController(JwtService jwtService) {
      this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registration(@Valid @RequestBody UserDto userDto) {
      User existingUser = userService.findUserByEmail(userDto.getEmail());
      if (existingUser != null) {
        return ResponseEntity.badRequest().body("There is already an account registered with the same email");
      }

      existingUser = userService.findUserByUsername(userDto.getUsername());
      if (existingUser != null) {
        return ResponseEntity.badRequest().body("There is already an account registered with the same username");
      }

      userService.saveUser(userDto);
      return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginDto loginDto) {
      User authenticatedUser = userService.findUserByUsername(loginDto.getUsername());
      // TODO: inject PasswordEncoder instead of creating a new instance
      if (authenticatedUser == null || !new BCryptPasswordEncoder().matches(loginDto.getPassword(), authenticatedUser.getPassword())) {
        return ResponseEntity.badRequest().body("Invalid username or password");
      }
      String jwtToken = jwtService.generateToken(authenticatedUser);
      LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
      return ResponseEntity.ok(loginResponse);
    }

}
