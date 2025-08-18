package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.AuditAction;
import com.minseop.admin_backoffice.domain.AuditLog;
import com.minseop.admin_backoffice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public void logChange(Long adminId, Long targetUserId, String targetUserUsername, AuditAction action, String beforeValue, String afterValue, String adminUsername) {
        AuditLog log = AuditLog.builder()
                .adminId(adminId)
                .targetUserId(targetUserId)
                .targetUserUsername(targetUserUsername)
                .action(action)
                .beforeValue(beforeValue)
                .afterValue(afterValue)
                .adminUsername(adminUsername)
                .build();

        auditLogRepository.save(log);
    }

    public Page<AuditLog> getAuditLogs(Long targetUserId, Pageable pageable) {
        if (targetUserId != null) {
            return auditLogRepository.findByTargetUserIdOrderByCreatedAtDesc(targetUserId, pageable);
        } else {
            return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
    }

}
