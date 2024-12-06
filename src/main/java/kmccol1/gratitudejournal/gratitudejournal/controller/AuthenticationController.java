//***************************************************************************************
//
//   Filename: AuthenticationController.java
//   Author: Kyle McColgan
//   Date: 04 December 2024
//   Description: This file provides register and login functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.controller;

import kmccol1.gratitudejournal.gratitudejournal.dto.UserAuthenticationDTO;
import kmccol1.gratitudejournal.gratitudejournal.dto.UserRegistrationDTO;
import kmccol1.gratitudejournal.gratitudejournal.model.User;
import kmccol1.gratitudejournal.gratitudejournal.payload.JwtResponse;
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

//***************************************************************************************

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO)
    {
        try
        {
            // Register the user using UserService with the new DTO
            User registeredUser = userService.registerUser(userRegistrationDTO);  // Pass DTO instead of RegisterRequest

            // Instead of authenticating the user immediately, just return success
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully.");
            return ResponseEntity.ok(response);
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
    public ResponseEntity<?> authenticateUser(@RequestBody UserAuthenticationDTO authenticationDTO)
    {
        // Authenticate the user using the provided credentials from DTO
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get the user details (from UserAuthenticationService)
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(userDetails.getId(), userDetails.getUsername(), userDetails.getAuthorities());

        // Create and return JWT response
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
