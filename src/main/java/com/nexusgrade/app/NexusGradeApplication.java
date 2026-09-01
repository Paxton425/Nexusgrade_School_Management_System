package com.nexusgrade.app;

import com.nexusgrade.app.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling; // <-- Add this import

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class NexusGradeApplication {

    public static Logger logger = LoggerFactory.getLogger(NexusGradeApplication.class);

    // Cleaner approach: let Spring pass it directly into the listener method
    public static void main(String[] args) {
        SpringApplication.run(NexusGradeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup(ApplicationReadyEvent event) {
        startUpCache(event, false);
    }

    public static void startUpCache(ApplicationReadyEvent event, boolean enabled){
        if(enabled){
            // Grab the service straight out of the initialized application context safely
            DashboardService dashboardService = event.getApplicationContext().getBean(DashboardService.class);

            logger.info("🚀 Warmup: Initializing Dashboard Cache on Application Startup...");
            dashboardService.generateAndCacheDashboard();
            logger.info("✅ Warmup Complete: Dashboard Cache is primed and ready!");
        }
        else logger.info("⚠\uFE0F Start Up Dashboard Cache Disabled!!");
    }
}