package com.nexusgrade.app.service;

import com.nexusgrade.app.model.ActivityLog;
import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.ActivityLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    Logger logger = LoggerFactory.getLogger(AuditService.class);

    public void log(User currentUser, String action, String entityType, String entityId, String details) {
        // In a real app, get the current logged-in user from SecurityContextHolder

        ActivityLog log = new ActivityLog(
                currentUser,
                action,
                entityType,
                entityId,
                details
        );
        activityLogRepository.save(log);

        if(currentUser.getFirstName() != null){
            String name = getInitials(currentUser.getFirstName(), currentUser.getMiddleName(), currentUser.getLastName());

            logger.info("""
                    \n============== ⚙️New Audit Record⚙️ ==============
                    \t{} {}
                    =====================================================
                    """
                    , name, action);
        }
    }

    private String getInitials(String fName, String mName, String lName){
        String fullName = "";
        if(fName != null) fullName += fName.charAt(0);
        if(mName != null) fullName += mName.charAt(0);
        if (lName != null) fullName = (fullName.toUpperCase())+" "+lName;

        if(fullName.equals("")) return "[Undefined User Name]";
        else return fullName;
    }
}
