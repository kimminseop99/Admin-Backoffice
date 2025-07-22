package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT COUNT(u) FROM UserEntity u")
    long countAllUsers();

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.createdAt >= :since")
    long countUsersRegisteredSince(@Param("since") LocalDateTime since);

    Optional<UserEntity> findByEmail(String email);
}
