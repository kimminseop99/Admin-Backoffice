package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.role NOT IN (:excludedRoles)")
    long countUsersExcludingRoles(@Param("excludedRoles") List<UserRole> excludedRoles);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.createdAt >= :startDate AND u.role NOT IN (:excludedRoles)")
    long countNewUsersLast7DaysExcludingRoles(@Param("startDate") LocalDateTime startDate,
                                              @Param("excludedRoles") List<UserRole> excludedRoles);


    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByUsernameContainingIgnoreCaseAndRoleNotIn(
            String username,
            List<UserRole> excludedRoles,
            Pageable pageable
    );

    Page<UserEntity> findByRoleNotIn(
            List<UserRole> excludedRoles,
            Pageable pageable
    );

    @Query("SELECT u.id FROM UserEntity u WHERE u.username = :username")
    Optional<Long> findUserIdByUsername(@Param("username") String username);

}
