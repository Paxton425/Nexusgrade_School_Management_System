package com.nexusgrade.app.service;

import com.nexusgrade.app.dto.ActivityLogDTO;
import com.nexusgrade.app.dto.AssessmentScoreDTO;
import com.nexusgrade.app.dto.MarkDTO;
import com.nexusgrade.app.dto.StatsDTO;
import com.nexusgrade.app.event.EntityUpdatedEvent;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    GradingService gradingService;
    @Autowired
    StatisticsService statisticsService;

    private StudentRepository studentRepository;
    private InstructorRepository instructorRepository;
    private AssessmentRepository assessmentRepository;
    private AssessmentScoreRepository assessmentScoreRepository;
    private ActivityLogRepository activityLogRepository;
    private SubjectRepository subjectRepository;
    private StatsRepository statsRepository;
    private AcademicCalendarRepository calendarRepository;
    private AssessmentMarkRepository assessmentMarkRepository;

    // Standard single-threaded scheduler for managing the in-memory debounce delays
    private final ScheduledExecutorService debounceScheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pendingRefreshTask;

    DashboardService(StudentRepository studentRepository,
                     InstructorRepository instructorRepository,
                     AssessmentRepository assessmentRepository,
                     AssessmentScoreRepository assessmentScoreRepository,
                     AssessmentMarkRepository assessmentMarkRepository,
                     ActivityLogRepository activityLogRepository,
                     SubjectRepository subjectRepository,
                     StatsRepository statsRepository,
                     AcademicCalendarRepository calendarRepository){
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.assessmentRepository = assessmentRepository;
        this.assessmentMarkRepository = assessmentMarkRepository;
        this.assessmentScoreRepository = assessmentScoreRepository;
        this.activityLogRepository = activityLogRepository;
        this.subjectRepository = subjectRepository;
        this.statsRepository = statsRepository;
        this.calendarRepository = calendarRepository;
    }

    // This acts as instant-access thread-safe memory bucket
    private Map<String, Object> cachedDashboardData = new ConcurrentHashMap<>();
    Logger logger = LoggerFactory.getLogger(DashboardService.class);

    // Returns the data INSTANTLY without touching the database
    public Map<String, Object> getDashboardDataFromCache() {
        if (cachedDashboardData.isEmpty()) {
            generateAndCacheDashboard(); // Fallback if cache is empty
        }
        return cachedDashboardData;
    }

    @Async // ⚡ Runs on a background thread automatically on Log entity changes!
    @EventListener
    public synchronized void handleEntityChangeEvent(EntityUpdatedEvent event) {
        logger.info("🔄 Entity change detected for: {}. Scheduling debounced refresh...", event.getEntityName());

        // 1. Cancel the previously scheduled refresh task if it hasn't run yet
        if (pendingRefreshTask != null && !pendingRefreshTask.isDone()) {
            pendingRefreshTask.cancel(false);
        }

        // 2. Schedule a new task to execute exactly 5 seconds from now
        pendingRefreshTask = debounceScheduler.schedule(() -> {
            logger.info("🚀 5-second silence window reached. Executing heavy dashboard cache refresh now...");
            generateAndCacheDashboard();
        }, 5, TimeUnit.SECONDS);
    }

    // Every 15 minutes, the background thread wakes up
    @Scheduled(fixedRate = 900000)
    public synchronized void generateAndCacheDashboard() {
        try{
            Map<String, Object> freshData = new HashMap<>();
            Term currentTerm = calendarRepository.findCurrentTermsCalender()
                    .orElseThrow(()-> new EntityNotFoundException("Academic calender entity not found for current term."))
                    .getCurrentTerm();

            Map<String, Stats> stats = getStats(currentTerm);
            freshData.put("stats", stats);

            Map<String, Map<String, Map<String, Object>>> subjectPerformances = getSubjectPerformances(10);
            freshData.put("subjectPerformances", subjectPerformances);

            Map<Integer, Double> averagesPerGrade = getAveragesPerGrade();
            freshData.put("averagesPerGrade", averagesPerGrade);

            Map<String, Number[]> termAveragesPerGrade = getTermAveragesPerGrade();
            freshData.put("termAveragesPerGrade", termAveragesPerGrade);

            Map<String, Double> performanceDistribution = getPerformanceDistribution();
            freshData.put("performanceDistribution", performanceDistribution);

            // High-speed Top 5 directly leveraging SQL LIMIT
            freshData.put("topFiveStudents", getTop5Performers());

            // Recent activities
            freshData.put("activityLogs", getRecentActivities());

            // Swap the cache reference instantly with zero downtime
            this.cachedDashboardData = freshData;
        } catch(Exception e){
            logger.error("\nDashboard Cache Generation Failure!");
            e.printStackTrace();
        }

    }
    private List<MarkDTO> getTop5Performers(){
        return assessmentMarkRepository.findTop5ByOrderByScoreDesc().stream()
                .map(MarkDTO::new).collect(Collectors.toList());
    }

    private Map<String, Stats> getStats(Term term){
        // 1. Basic Stats via database level math
        Stats currentTermStats =  statsRepository.findFirstByAcademicCalendarTermOrderByCreatedAtDesc(term)
                .orElseGet(()-> statisticsService.generateNewLatestStats());
        Stats prevTermStats = statsRepository.findFirstBefore(currentTermStats.getId()-1) //Get preceding stats entry
                .orElseGet(Stats::new);

        return Map.of(
                "prevTermSats", currentTermStats,
                "currTermStats", prevTermStats
        );
    }

    public Map<Integer, Double> getAveragesPerGrade() {
        List<Object[]> dataRows = assessmentScoreRepository.getAverageMarkPerGrade();
        Map<Integer, Double> scores = new TreeMap<>();

        for (Object[] row : dataRows) {
            if (row != null && row.length >= 2) {
                Integer grade = (Integer) row[0];

                // Safe conversion handling both Double and BigDecimal database outputs
                Double average = row[3] instanceof Number ? ((Number) row[3]).doubleValue() : 0.0;
                scores.put(grade, average);
            }
        }
        return scores;
    }

    public Map<String, Number[]> getTermAveragesPerGrade() {
        try{
            List<Object[]> dataRows = assessmentScoreRepository.getTermAveragesPerGrade(Term.TERM_2.toString(), Term.TERM_1.toString());
            Map<String, Number[]> scores = new TreeMap<>(); // TreeMap automatic sorting
            Integer[] grades = new Integer[dataRows.size()/2];
            scores.put("Grades", grades);
            int gradesIndex = 0;

            for (Object[] row : dataRows) {
                Integer grade = (Integer) row[0];
                grades[gradesIndex] = (grades[gradesIndex] == null)? grade : grades[gradesIndex++];
                if (row != null && row.length >= 3) {
                    String term = Arrays.stream(Term.values())
                            .filter(t -> t.toString().equals(row[1].toString()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("No matching term found for: " + row[1].toString()))
                            .toString();
                    Number average = row[2] instanceof Number ? ((Number) row[2]) : 0.0;

                    if(!scores.isEmpty() && scores.containsKey(term)){
                        Number[] averages = scores.get(term);
                        for(int i=0; i<averages.length; i++)
                            if(averages[i] == null){
                                averages[i] = average;
                                break;
                            }
                        scores.put(term, averages);
                    }
                    else {
                        Number[] newAverages = new Number[dataRows.size()/2];
                        newAverages[0] = average;
                        scores.put(term, newAverages);
                    }
                }
            }
            return scores;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public Map<String, Double> getPerformanceDistribution(){
        try{

            Map<String, Object> performanceDist = assessmentScoreRepository.getPerformanceLevelDistribution();

            Number poorCount = (Number) performanceDist.get("poor");
            Number badCount = (Number) performanceDist.get("bad");
            Number averageCount = (Number) performanceDist.get("average");
            Number goodCount = (Number) performanceDist.get("good");
            Number excellentCount = (Number) performanceDist.get("excellent");

            Number rawTotalSubmisions = (Number) performanceDist.get("totalResults");
            long totalSubmissions = (rawTotalSubmisions !=null )? rawTotalSubmisions.longValue() : 0L;

            return Map.of(
                    "poor", getPercent(poorCount.longValue(), totalSubmissions),
                    "bad", getPercent(badCount.longValue(), totalSubmissions),
                    "average", getPercent(averageCount.longValue(), totalSubmissions),
                    "good", getPercent(goodCount.longValue(), totalSubmissions),
                    "excellent", getPercent(excellentCount.longValue(), totalSubmissions)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Map<String, Map<String, Object>>> getSubjectPerformances(int grade){
        try{
            List<Object[]> rawResults = assessmentScoreRepository.findSubjectPerformanceByTermsAndGrade(List.of("TERM_2", "TERM_1"), 10);

            Map<String, Map<String, Map<String, Object>>> finalResult = new LinkedHashMap<>();

            for (Object[] row : rawResults) {
                String term = (String) row[0];
                String subjectCode = (String) row[2];

                Map<String, Object> subjectData = new LinkedHashMap<>();
                subjectData.put("id", row[1]);
                subjectData.put("name", row[3]);
                subjectData.put("totalScore", row[4]);
                subjectData.put("maxTotalScore", row[5]);
                subjectData.put("performance", row[6]);

                finalResult.computeIfAbsent(term, k -> new LinkedHashMap<>())
                        .put(subjectCode, subjectData);
            }

            return finalResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ActivityLogDTO> getRecentActivities(){
        return activityLogRepository.findRecentActivities(5)
                .stream().map(ActivityLogDTO::new).collect(Collectors.toList());
    }

    static double getPercent(long amount, long max){
        if(max<1) return 0.0;
        double result = ((double) amount / max) * 100;
        return Math.round(result*100.0)/100.0; //Rounded to 2dp
    }
}