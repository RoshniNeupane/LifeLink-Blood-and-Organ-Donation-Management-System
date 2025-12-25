package com.lifelink.config;

import java.util.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
import com.lifelink.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String login = oAuth2User.getAttribute("login");
        String avatarUrl = oAuth2User.getAttribute("avatar_url");

        // Generate a unique email if missing
        if (email == null || email.isEmpty()) {
            if (login != null && !login.isEmpty()) email = login + "@oauthuser.com";
            else if (name != null && !name.isEmpty()) email = name.toLowerCase().replaceAll("\\s+", "") + "@oauthuser.com";
            else email = UUID.randomUUID().toString() + "@oauthuser.com";
        }

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            // New user: default role USER
            user = new Users();
            user.setEmail(email);
            user.setFullName(name != null ? name : "OAuth User");
            user.setRole(Role.USER); // web-only roles: ADMIN, USER, HOSPITAL
            user.setPassword(passwordEncoder.encode("OAuthUser123")); 
            user.setProfileImagePath(picture != null ? picture : (avatarUrl != null ? avatarUrl : "/uploads/blank_profile.jpg"));
        } else {
            // Existing user: update name and picture
            if (name != null) user.setFullName(name);
            if (picture != null) user.setProfileImagePath(picture);
            else if (avatarUrl != null) user.setProfileImagePath(avatarUrl);
        }

        userRepository.save(user);

        // Map authorities
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("email", email);

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
            attributes,
            "email"
        );
    }
}
