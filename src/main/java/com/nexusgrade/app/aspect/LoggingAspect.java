package com.nexusgrade.app.aspect;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.UserRepository;
import com.nexusgrade.app.service.AuditService;
import com.nexusgrade.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Autowired
    private AuditService auditService;
    @Autowired
    UserRepository userRepository;

    // This runs only IF the method returns successfully (no errors)
    @AfterReturning(pointcut = "@annotation(logActivity)", returning = "result")
    public void logAfter(JoinPoint joinPoint, LogActivity logActivity, Object result) {
        try{
            // You can get method arguments (like studentId) from joinPoint
            Object[] args = joinPoint.getArgs();
            String id = (args.length > 0) ? args[0].toString() : "N/A";

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null)
                throw new NullPointerException("Authentication data is null");
            if (!authentication.isAuthenticated())
                throw new Exception("User is not authenticated!");
            if(authentication.getName() == null || (authentication.getName().trim()) == "")
                throw new EntityNotFoundException("Could not capture user who performed action!");
            else {
                logger.info("============= CAPTURED USER ====================\n{}", authentication.getDetails());
                logger.info("USERNAME: {}", authentication.getName());

                User  loggedInUser = userRepository.findByUsername(authentication.getName())
                        .orElseThrow(()-> new EntityNotFoundException("Error retrieving user!"));

                String details = "Action performed on " + logActivity.entityType();

                auditService.log(
                        loggedInUser,
                        logActivity.action(),
                        logActivity.entityType(),
                        id,
                        details
                );
            }
        } catch (Exception e) {
            logger.error("Error logging details for action perfomed by user! ");
            e.printStackTrace();
        }
    }
}

