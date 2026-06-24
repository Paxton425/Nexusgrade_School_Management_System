package com.nexusgrade.app;

import com.nexusgrade.app.service.DashboardService;
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

    // Cleaner approach: let Spring pass it directly into the listener method
    public static void main(String[] args) {
        SpringApplication.run(NexusGradeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup(ApplicationReadyEvent event) {
        // Grab the service straight out of the initialized application context safely
        DashboardService dashboardService = event.getApplicationContext().getBean(DashboardService.class);

        System.out.println("🚀 Warmup: Initializing Dashboard Cache on Application Startup...");
        dashboardService.generateAndCacheDashboard();
        System.out.println("✅ Warmup Complete: Dashboard Cache is primed and ready!");
    }
}