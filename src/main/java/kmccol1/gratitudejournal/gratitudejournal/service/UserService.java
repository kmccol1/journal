//***************************************************************************************
//
//     Filename: UserService.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file provides abstracted registration functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import kmccol1.gratitudejournal.gratitudejournal.dto.UserRegistrationDTO;
import kmccol1.gratitudejournal.gratitudejournal.model.Role;
import kmccol1.gratitudejournal.gratitudejournal.model.User;
import kmccol1.gratitudejournal.gratitudejournal.repository.RoleRepository;
import kmccol1.gratitudejournal.gratitudejournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//***************************************************************************************

@Service
public class UserService implements UserRetrievalService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO)
    {
        // Check for existing username or email
        if (userRepository.existsByUsername(registrationDTO.getUsername()))
        {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail()))
        {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user
        User user = new User(
                registrationDTO.getUsername(),
                registrationDTO.getEmail(),
                passwordEncoder.encode(registrationDTO.getPassword())
        );

        // Assign roles - by default, every user gets "ROLE_USER"
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null)
        {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @PostConstruct
    public void initRoles()
    {
        if (roleRepository.count() == 0)
        {
            Role roleUser = new Role("ROLE_USER");
            Role roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
        }
    }

    public boolean userExists(Integer userId)
    {
        return userRepository.existsById(userId);
    }
}
//***************************************************************************************
