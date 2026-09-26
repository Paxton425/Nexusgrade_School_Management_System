package com.nexusgrade.app.aspect;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.UserRepository;
import com.nexusgrade.app.service.AuditService;
import com.nexusgrade.app.service.GeoLocationService; // Optional Geo Service
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private GeoLocationService geoService; // Inject your Geo API/MaxMind service here

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    @AfterReturning(pointcut = "@annotation(logActivity)", returning = "result")
    public void logAfter(JoinPoint joinPoint, LogActivity logActivity, Object result) {
        try {
            Object[] args = joinPoint.getArgs();
            String id = (args.length > 0 && args[0] != null) ? args[0].toString() : "N/A";

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null || authentication.getName().trim().isEmpty()) {
                throw new EntityNotFoundException("Could not capture authenticated user!");
            }

            User loggedInUser = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Error retrieving user!"));

            // 1. Capture Http Servlet Request from context
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            String clientIp = "UNKNOWN";
            String userAgent = "UNKNOWN";
            String location = "UNKNOWN";

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                clientIp = extractClientIp(request);
                userAgent = request.getHeader("User-Agent");

                // 2. Resolve Geolocation (if service present)
                if (geoService != null && !isLocalIp(clientIp)) {
                    location = geoService.getLocationSummary(clientIp); // e.g., "Durban, South Africa"
                } else if (isLocalIp(clientIp)) {
                    location = "Local Network / Localhost";
                }
            }

            // 3. Build Detailed Audit Entry
            String details = String.format("Action: %s | Entity: %s | Target ID: %s | IP: %s | Location: %s | User-Agent: %s",
                    logActivity.action(), logActivity.entityType(), id, clientIp, location, userAgent);

            logger.info("AUDIT LOG: User [{}] from IP [{}] ({}) executed [{}] on [{}]",
                    loggedInUser.getUsername(), clientIp, location, logActivity.action(), logActivity.entityType());

            // 4. Pass extra info to AuditService
            auditService.log(
                    loggedInUser,
                    logActivity.action(),
                    logActivity.entityType(),
                    id,
                    details
            );

        } catch (Exception e) {
            logger.error("Error logging details for action performed by user!", e);
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    private boolean isLocalIp(String ip) {
        return "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || ip.startsWith("192.168.") || ip.startsWith("10.");
    }
}