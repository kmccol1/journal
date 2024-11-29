//***************************************************************************************
//
//   Filename: AuthenticationController.java
//   Author: Kyle McColgan
//   Date: 27 November 2024
//   Description: This file provides register and login functionality.
//
//***************************************************************************************


package kmccol1.gratitudejournal.gratitudejournal.controller;

import kmccol1.gratitudejournal.gratitudejournal.model.User;
import kmccol1.gratitudejournal.gratitudejournal.payload.JwtResponse;
import kmccol1.gratitudejournal.gratitudejournal.payload.LoginRequest;
import kmccol1.gratitudejournal.gratitudejournal.payload.RegisterRequest;
import kmccol1.gratitudejournal.gratitudejournal.security.UserDetailsImpl;
import kmccol1.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import kmccol1.gratitudejournal.gratitudejournal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
    {
        try
        {
            // Register the user
            User registeredUser = userService.registerUser(registerRequest);

            // Authenticate the new user to generate a JWT token
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registerRequest.getUsername(),
                            registerRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Generate a JWT token...with authorities...
            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), userDetails.getAuthorities());

            // Create and return the JwtResponse with the token and user details
            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getAuthorities()
            );

            return ResponseEntity.ok(jwtResponse);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace(); // For debugging purposes
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Registration error occurred: ", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate token with roles
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), userDetails.getAuthorities());

        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities()
        );

        return ResponseEntity.ok(jwtResponse);
    }
}

//***************************************************************************************
