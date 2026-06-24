package com.nexusgrade.app.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class AssessmentService {

    public class TimeProgess{
        public Long totalDays;
        public Long daysPassed;
        public Long daysLeft;
        public Double progress;
        public String statusText;

        public TimeProgess(Long totalDays, Long daysPassed, Long daysLeft, double progress, String statusText) {
            this.totalDays = totalDays;
            this.daysPassed = daysPassed;
            this.daysLeft = daysLeft;
            this.progress = progress;
            this.statusText = statusText;
        }
    }

    public TimeProgess calculateTimeProgress(LocalDate issue, LocalDate deadline) {
        LocalDate now = LocalDate.now();
        Long totalDays = ChronoUnit.DAYS.between(issue, deadline);
        Long daysPassed = ChronoUnit.DAYS.between(issue, now);
        Long daysLeft = ChronoUnit.DAYS.between(now, deadline);

        // Calculate percentage (clamped between 0 and 100)
        double progress = (totalDays <= 0) ? 100 : (double) daysPassed / totalDays * 100;
        progress = Math.max(0, Math.min(100, progress));

        String statusText = "";
        if(daysLeft != null){
            if (daysLeft < 0) {
                statusText = "Overdue by " + Math.abs(daysLeft) + " days";
            } else if (daysLeft == 0) {
                statusText = "Due Today!";
            } else {
                statusText = daysLeft + " days left";
            }
        }

        return  new TimeProgess(totalDays, daysPassed, daysLeft, progress, statusText);
    }
}
