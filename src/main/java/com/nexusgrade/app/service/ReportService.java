package com.nexusgrade.app.service;

import com.nexusgrade.app.dto.*;
import com.nexusgrade.app.dto.StudentReportDTO;
import com.nexusgrade.app.dto.StudentReportDTO.TermResult;
import com.nexusgrade.app.model.Result.*;
import com.nexusgrade.app.model.Result.Term;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Subject;
import com.nexusgrade.app.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {
    @Autowired
    private GradingService gradingService;
    ReportRepository reportRepository;
    public ReportService(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }

    public StudentReport generateNewReportTemplate(Student student)
    throws RuntimeException{
        try{
            List<Subject> subjects = student.getSchoolClass().getSubjects();
            StudentReport report = new StudentReport(null, 0.0, 0.0, 0.0, 0.0, student, null);
            List<StudentTermReport> termReports = new ArrayList<>();
            for(Term term: Term.values()){
                for(Subject subject: subjects){
                    termReports.add(new StudentTermReport(null, 0.0, 0.0, 1, subject, null, term));
                }
            }
            report.setTermResults(termReports);
            return reportRepository.save(report);
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Report Template Generation Failure");
        }
    }

    public Map<Term, Double[]> getTermOveralls(StudentReport report) {
        List<StudentTermReport> termResults = report.getTermResults();

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
                for (StudentTermReport tr : termResults) {
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
}