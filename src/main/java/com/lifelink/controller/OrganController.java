//package com.lifelink.controller;
//
//import com.lifelink.entity.OrganRequest;
//import com.lifelink.entity.User;
//import com.lifelink.repository.OrganRepository;
//import com.lifelink.repository.UserRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/organ")
//@CrossOrigin
//public class OrganController {
//
//    private final OrganRepository organRepo;
//    private final UserRepository userRepo;
//
//    public OrganController(OrganRepository organRepo, UserRepository userRepo) {
//        this.organRepo = organRepo;
//        this.userRepo = userRepo;
//    }
//
//    @PostMapping("/request")
//    public OrganRequest createRequest(Authentication auth, @RequestBody OrganRequest req) {
//        User user = userRepo.findByUsername(auth.getName()).orElse(null);
//        if (user != null) {
//            req.setRequestedBy(user);
//            organRepo.save(req);
//        }
//        return req;
//    }
//
//    @GetMapping("/all")
//    public List<OrganRequest> all() { return organRepo.findAll(); }
//}
