package com.lifelink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
import com.lifelink.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Users registerUser(Users user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null; // user already exists
        }

        if (user.getProfileImagePath() == null || user.getProfileImagePath().isEmpty()) {
            user.setProfileImagePath("/uploads/blank_profile.jpg");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    
    public int countAllUsers() {
        return (int) userRepository.count();
    }
    // ========== Find user by email ==========
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ========== Find user by ID ==========
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ========== Save or update user ==========
    public void save(Users user) {
        userRepository.save(user);
    }

    // ========== Delete user ==========
    public void delete(Users user) {
        userRepository.delete(user);
    }

    // ========== Find users by role ==========
    public List<Users> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }
    public void registerOAuthUser(Users user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            if (user.getProfileImagePath() == null || user.getProfileImagePath().isEmpty()) {
                user.setProfileImagePath("/uploads/blank_profile.jpg");
            }
            
            if (user.getRole() == null) {
                user.setRole(Role.USER);
            }

            user.setPassword(passwordEncoder.encode("oauth_dummy_password"));

            userRepository.save(user);
        }
    }

    public long countByRole(Role role) {
        return userRepository.countByRole(role);
    }

    // ========== Spring Security integration ==========
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()) // ADMIN, USER, HOSPITAL
                .build();
    }
}
