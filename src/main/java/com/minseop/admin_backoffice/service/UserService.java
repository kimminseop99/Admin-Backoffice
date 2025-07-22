package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Page<UserEntity> findAllUsers(String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findAll(
                Example.of(UserEntity.builder()
                                .username(searchKeyword)
                                .build(),
                        ExampleMatcher.matchingAny()
                                .withIgnorePaths("id", "password", "email", "fullName", "status", "role", "createdAt", "updatedAt")
                                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                ), pageable);
    }


    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }


    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public long getTotalUserCount() {
        return userRepository.countAllUsers();
    }


    public long getNewUsersCountLast7Days() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return userRepository.countUsersRegisteredSince(sevenDaysAgo);
    }


    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
