package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.AuditLog;
import com.minseop.admin_backoffice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/audit/logs")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public String viewAuditLogs(
            @RequestParam(name = "targetUserId", required = false) Long targetUserId,
            Pageable pageable,
            Model model
    ) {
        Page<AuditLog> logs = auditLogService.getAuditLogs(targetUserId, pageable);
        model.addAttribute("logs", logs);
        model.addAttribute("targetUserId", targetUserId);
        return "audit/log_list";
    }
}