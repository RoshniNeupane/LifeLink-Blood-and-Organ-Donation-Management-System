package com.lifelink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lifelink.entity.Role;
import com.lifelink.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // Find all users by role
    List<Users> findByRole(Role role);

    Users findByResetPasswordToken(String token);
    // Count users by role
    long countByRole(Role role);

    // Find all users by role (alternative method)
    List<Users> findAllByRole(Role role);

    // Find user by email
    Users findByEmail(String email);
    

    
}
