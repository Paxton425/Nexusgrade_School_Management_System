package com.nexusgrade.app.service;

import com.nexusgrade.app.model.ActivityLog;
import com.nexusgrade.app.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void log(String action, String entityType, String entityId, String details) {
        // In a real app, get the current logged-in user from SecurityContextHolder
        String currentUser = "Admin_User";

        ActivityLog log = new ActivityLog(
                currentUser,
                action,
                entityType,
                entityId,
                details
        );

        activityLogRepository.save(log);
    }
}
