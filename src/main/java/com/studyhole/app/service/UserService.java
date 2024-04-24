package com.studyhole.app.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.studyhole.app.model.User;
import com.studyhole.app.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    
    @Transactional
    public Optional<User> fetchUserOptional(String username){
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username).
        orElseThrow(() -> new UsernameNotFoundException(username + "does not exist!")));
        return user;
    }

    @Transactional
    public User getCurrentUser(){
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
        .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }
}