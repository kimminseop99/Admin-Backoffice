package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.AuditAction;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.domain.UserRole;
import com.minseop.admin_backoffice.domain.UserStatus;
import com.minseop.admin_backoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

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
        return userRepository.countUsersExcludingRoles(List.of(UserRole.ADMIN, UserRole.SUPER_ADMIN));
    }

    public long getNewUsersCountLast7Days() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return userRepository.countNewUsersLast7DaysExcludingRoles(
                sevenDaysAgo,
                List.of(UserRole.ADMIN, UserRole.SUPER_ADMIN)
        );
    }


    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<UserEntity> findUsersExcludingRoles(String searchKeyword, Pageable pageable, List<UserRole> excludedRoles) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return userRepository.findByRoleNotIn(excludedRoles, pageable);
        }
        return userRepository.findByUsernameContainingIgnoreCaseAndRoleNotIn(searchKeyword, excludedRoles, pageable);
    }

    public void updateUserStatusOrRole(UserEntity targetUser, UserEntity currentAdmin, UserStatus newStatus, UserRole newRole) {
        if (targetUser.getStatus() != newStatus) {
            auditLogService.logChange(
                    currentAdmin.getId(),
                    targetUser.getId(),
                    AuditAction.STATUS_CHANGE,
                    targetUser.getStatus().name(),
                    newStatus.name(),
                    currentAdmin.getUsername()
            );
            targetUser.setStatus(newStatus);
        }

        if (targetUser.getRole() != newRole) {
            auditLogService.logChange(
                    currentAdmin.getId(),
                    targetUser.getId(),
                    AuditAction.ROLE_CHANGE,
                    targetUser.getRole().name(),
                    newRole.name(),
                    currentAdmin.getUsername()
            );
            targetUser.setRole(newRole);
        }

        userRepository.save(targetUser);
    }


    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + username));
    }
}
