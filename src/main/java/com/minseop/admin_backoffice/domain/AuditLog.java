package com.minseop.admin_backoffice.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adminId;  // 변경을 수행한 관리자

    @Column(nullable = false, length = 50)
    private String adminUsername;

    private Long targetUserId;  // 변경 대상 사용자

    @Enumerated(EnumType.STRING)
    private AuditAction action;  // ROLE_CHANGE, STATUS_CHANGE 등

    private String beforeValue;

    private String afterValue;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
