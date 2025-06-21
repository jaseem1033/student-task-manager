package com.student.taskmanagement.controller;

import com.student.taskmanagement.dto.AuthRequest;
import com.student.taskmanagement.dto.AuthResponse;
import com.student.taskmanagement.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String token = jwtService.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password.");
        }
        catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User account is disabled.");
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("An unexpected error occurred during authentication: " + e.getMessage());
            e.printStackTrace(); // For development, remove in production
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected server error occurred.");
        }
    }
}