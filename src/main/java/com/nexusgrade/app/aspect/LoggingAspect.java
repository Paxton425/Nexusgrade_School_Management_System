package com.nexusgrade.app.aspect;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.service.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AuditService auditService;

    // This runs only IF the method returns successfully (no errors)
    @AfterReturning(pointcut = "@annotation(logActivity)", returning = "result")
    public void logAfter(JoinPoint joinPoint, LogActivity logActivity, Object result) {

        // You can get method arguments (like studentId) from joinPoint
        Object[] args = joinPoint.getArgs();
        String id = (args.length > 0) ? args[0].toString() : "N/A";

        String details = "Action performed on " + logActivity.entityType();

        auditService.log(
                logActivity.action(),
                logActivity.entityType(),
                id,
                details
        );
    }
}


