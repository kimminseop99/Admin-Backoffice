package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.minseop.admin_backoffice.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
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

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
