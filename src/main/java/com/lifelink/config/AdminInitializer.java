package com.lifelink.config;

import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
import com.lifelink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Initializes default ADMIN user on application startup
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Check if admin already exists
        if (userRepository.findByEmail("admin@lifelink.com") != null) {
            System.out.println("✓ Admin user already exists");
            return;
        }

        // Create default admin user
        Users admin = new Users();
        admin.setFullName("System Administrator");
        admin.setEmail("admin@lifelink.com");
        admin.setPhoneNumber("9800000000");
        admin.setPassword(passwordEncoder.encode("Admin@123")); // Change after first login
        admin.setGender("Other");
        admin.setRole(Role.ADMIN); // Use enum
        admin.setFraudFlag(false); // optional
        admin.setProfileImagePath("/uploads/blank_profile.jpg");
        
       
        userRepository.save(admin);

        System.out.println("========================================");
        System.out.println("✓ Default ADMIN user created successfully!");
        System.out.println("Email: admin@lifelink.com");
        System.out.println("Password: Admin@123");
        System.out.println("⚠️  IMPORTANT: Change this password immediately after first login!");
        System.out.println("========================================");
    }
}
