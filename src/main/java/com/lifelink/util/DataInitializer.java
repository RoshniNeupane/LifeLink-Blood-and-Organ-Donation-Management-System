//package com.lifelink.util;
//
//import com.lifelink.entity.User;
//import com.lifelink.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepo;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    public DataInitializer(UserRepository userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (userRepo.findByUsername("admin").isEmpty()) {
//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setPassword(encoder.encode("admin123"));
//            admin.setFullName("Super Admin");
//            admin.setEmail("admin@lifeline.com");
//            admin.setRole("ADMIN");
//            admin.setFingerprintId("admin-fp-1"); // sample
//            userRepo.save(admin);
//        }
//    }
//}
