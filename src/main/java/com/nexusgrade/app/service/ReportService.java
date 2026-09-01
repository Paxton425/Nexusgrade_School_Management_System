package com.nexusgrade.app.service;

import com.nexusgrade.app.model.Term;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Subject;
import com.nexusgrade.app.repository.ReportRepository;
import com.nexusgrade.app.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private GradingService gradingService;

    ReportRepository reportRepository;
    StudentRepository studentRepository;
    public ReportService(ReportRepository reportRepository,
                         StudentRepository studentRepository
    ){
        this.reportRepository = reportRepository;
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(ReportService.class);

    public Report generateNewReportTemplate(Student student)
        throws RuntimeException{
        try{
            Report report = new Report(null,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    student,
                    student.getSchoolClass().getGrade(),
                    LocalDate.now().getYear(),
                    null);

            return loadReportData(student, report);

        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Report Template Generation Failure!");
        }
    }

    public Report refreshStudentReport(Student student){
        Report currentYearReport = student.getReports()
                .stream().filter(r-> r.getAcademicYear() == LocalDate.now().getYear())
                .findFirst().orElse(null);

        if(currentYearReport != null)
            return loadReportData(student, currentYearReport);
        else
            return generateNewReportTemplate(student);
    }

    private Report loadReportData(Student student, Report report){
        //Creates entries of subject results per term
       try{
           List<Subject> subjects = student.getSchoolClass().getSubjects();
           List<TermReport> termReports = new ArrayList<>();
           for(Term term: Term.values()){

               for(Subject subject: subjects){
                   double finalMark = gradingService.calculateFinalMark(student.getAssessmentScores(), subject, term);
                   double average = gradingService.calculateTermAverage(student.getAssessmentScores(), term);
                   int gradeLevel = gradingService.calculateLevel(finalMark);

                   termReports.add(new TermReport(
                           null,
                           finalMark,
                           average,
                           gradeLevel,
                           subject,
                           null,
                           term));
               }
           }
           report.setTermResults(termReports);
           return reportRepository.save(report);
       } catch(Exception e) {
           logger.error("Report Data Loading Failure! \n{}", e.getMessage());
           throw new RuntimeException("Report Data Loading Error!");
       }
    }

    public Map<Term, Double[]> getTermOveralls(Report report) {
        List<TermReport> termResults = report.getTermResults();

        // Default Values - Map<Term, [total, average]>
        Map<Term, Double[]> termOveralls = new HashMap<>(Map.of(
                Term.TERM_1, new Double[]{0.0, 0.0},
                Term.TERM_2, new Double[]{0.0, 0.0},
                Term.TERM_3, new Double[]{0.0, 0.0},
                Term.TERM_4, new Double[]{0.0, 0.0}
        ));

        try {
            if (termResults != null && !termResults.isEmpty()) {
                // Step 1: Use an auxiliary map to count the number of subjects per term
                Map<Term, Integer> subjectCounts = new HashMap<>();

                // Step 2: Sum up the totals and track how many subjects are in each term
                for (TermReport tr : termResults) {
                    Term currentTerm = tr.getTerm();

                    if (currentTerm != null && termOveralls.containsKey(currentTerm)) {
                        Double[] currentData = termOveralls.get(currentTerm);
                        // Add the final grade to the current total sum
                        currentData[0] += tr.getFinalGrade();
                        // Increment the subject counter for this specific term
                        subjectCounts.put(currentTerm, subjectCounts.getOrDefault(currentTerm, 0) + 1);
                    }
                }

                // Step 3: Calculate the accurate mathematical average for each term
                termOveralls.forEach((term, data) -> {
                    int count = subjectCounts.getOrDefault(term, 0);
                    if (count > 0) {
                        data[1] = data[0] / count; // True overall average (Total / Number of Subjects)
                    }
                });
            }

            return termOveralls;

        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate term overall statistics", e);
        }
    }

    public List<Report> getRefreshedStudentReports(List<Student> students){
        List<Report> refreshedReports = new ArrayList<>();
        for (Student student : students) {
            try {
                // Check if current year report exists, if not create one
                Report existingReport = student.getReports()
                        .stream().filter(r -> r.getAcademicYear() == LocalDate.now().getYear())
                        .findFirst()
                        .orElse(null);
                Report refreshedReport;

                if (existingReport != null) {
                    refreshedReport = refreshStudentReport(student);
                } else {
                    refreshedReport = generateNewReportTemplate(student);
                }

                refreshedReports.add(refreshedReport);
                logger.info("Refreshed report for student: {} {}", student.getFirstName(), student.getLastName());

            } catch (Exception e) {
                logger.error("Failed to refresh report for student: {}", student.getId(), e);
                // Continue with other students even if one fails
            }
        }
        logger.info("Successfully refreshed {} student reports", refreshedReports.size());
        return refreshedReports;
    }

    public List<Report> refreshAllStudentReports() {
        List<Report> refreshedReports = new ArrayList<>();
        try {
            // Get all existing reports or all students
            List<Student> allStudents = studentRepository.findAll(); // You'll need to inject StudentRepository

            refreshedReports = getRefreshedStudentReports(allStudents);

            logger.info("Successfully refreshed {} student reports", refreshedReports.size());
            return refreshedReports;

        } catch (Exception e) {
            logger.error("Mass report refresh failed", e);
            throw new RuntimeException("Mass Report Refresh Failed", e);
        }
    }

    // Schedule to refresh all reports at 1 AM on week days(Monday - Friday)
    @Scheduled(cron = "0 0 1 * * MON-FRI")
    public void scheduleMassReportRefresh() {
        logger.info("Starting scheduled mass report refresh at 1 AM");
        try {
            this.refreshAllStudentReports();
            logger.info("Completed scheduled mass report refresh successfully");
        } catch (Exception e) {
            logger.error("Scheduled mass report refresh failed", e);
        }
    }
}