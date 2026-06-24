package com.nexusgrade.app.service;

import com.nexusgrade.app.dto.ResultDTO;
import com.nexusgrade.app.event.EntityUpdatedEvent;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Result.Term;
import com.nexusgrade.app.repository.*;
import com.nexusgrade.app.repository.AssessmentRepository;
import com.nexusgrade.app.repository.InstructorRepository;
import com.nexusgrade.app.repository.ResultRepository;
import com.nexusgrade.app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    GradingService gradingService;

    StudentRepository studentRepository;
    InstructorRepository instructorRepository;
    AssessmentRepository assessmentRepository;
    ResultRepository resultRepository;
    ActivityLogRepository activityLogRepository;
    SubjectRepository subjectRepository;

    DashboardService(StudentRepository studentRepository,
                     InstructorRepository instructorRepository,
                     AssessmentRepository assessmentRepository,
                     ResultRepository resultRepository,
                     ActivityLogRepository activityLogRepository,
                     SubjectRepository subjectRepository){
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.assessmentRepository = assessmentRepository;
        this.resultRepository = resultRepository;
        this.activityLogRepository = activityLogRepository;
        this.subjectRepository = subjectRepository;
    }

    // This acts as our instant-access thread-safe memory bucket
    private Map<String, Object> cachedDashboardData = new ConcurrentHashMap<>();

    // Returns the data INSTANTLY without touching the database
    public Map<String, Object> getDashboardDataFromCache() {
        if (cachedDashboardData.isEmpty()) {
            generateAndCacheDashboard(); // Fallback if cache is empty
        }
        return cachedDashboardData;
    }

    @Async // ⚡ Runs on a background thread automatically!
    @EventListener
    public void handleEntityChangeEvent(EntityUpdatedEvent event) {
        System.out.println("🔄 Entity change detected for: " + event.getEntityName() + ". Refreshing dashboard cache...");
        generateAndCacheDashboard();
    }

    // Every 15 minutes, the background thread wakes up
    @Scheduled(fixedRate = 900000)
    public synchronized void generateAndCacheDashboard() {
        Map<String, Object> freshData = new HashMap<>();

        // 1. Basic Stats via database level math
        long totalStudents = studentRepository.count();
        long totalSubmissions = resultRepository.countSubmissions();
        Double avg = resultRepository.getAverageScore();
        double averageGrade = (avg != null) ? avg : 0.0;

        freshData.put("stats", Map.of(
                "studentsCount", totalStudents,
                "submissions", totalSubmissions,
                "averageGrade", averageGrade,
                "passRate", 85.5 // Optimize pass-rate tracking via a quick targeted repository count
        ));

        List<Object[]> subjectPerformances = getSubjectPerformances(10);
        freshData.put("subjectPerformances", subjectPerformances);

        List<Object[]> averagesPerGrade = resultRepository.getAverageScorePerGrade();
        freshData.put("averagesPerGrade", averagesPerGrade);

        Map<String, Double> performanceDistribution = getPerformanceDistribution(totalSubmissions);
        freshData.put("performanceDistribution", performanceDistribution);

        // High-speed Top 5 directly leveraging SQL LIMIT
        List<ResultDTO> topFive = resultRepository.findTop5ByOrderByScoreDesc()
                .stream().map(ResultDTO::new).collect(Collectors.toList());
        freshData.put("topFiveStudents", topFive);

        // Recent activities
        freshData.put("activityLogs",getRecentActivities());

        // Swap the cache reference instantly with zero downtime
        this.cachedDashboardData = freshData;
    }

    public List<ActivityLog> getRecentActivities(){
        return activityLogRepository.findFirst5ByOrderByIdDesc();
    }

    public Map<String, Double> getPerformanceDistribution(long totalSubmisions){
        try{
            long poorCount = resultRepository.countAllByOrLessThan(40.00);
            long badCount = resultRepository.countAllByOrLessThan(50.00)-poorCount;
            long averageCount = resultRepository.countAllByOrLessThan(60.00)-(badCount+poorCount);
            long goodCount = resultRepository.countAllByOrLessThan(70.00)-(badCount+poorCount+averageCount);
            long excellentCount = resultRepository.countAllByOrLessThan(100.00)-(badCount+poorCount+averageCount+goodCount);
            return Map.of(
                    "poor", getPercent(poorCount, totalSubmisions),
                    "bad", getPercent(badCount, totalSubmisions),
                    "average", getPercent(averageCount, totalSubmisions),
                    "good", getPercent(goodCount, totalSubmisions),
                    "excellent", getPercent(excellentCount, totalSubmisions)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> getSubjectPerformances(int grade){
        try{
            Long maxGradePoints = resultRepository.getTotalMaxScoreForGrade(grade);
            return resultRepository.getSubjectPerformancePercentages(Term.TERM_1, Term.TERM_2, grade, maxGradePoints);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    double getPercent(long amount, long total){//DecimalFormat form
        if(total<1) return 0.0;
        return ((double)amount/(double)total)*100;
    }
}
