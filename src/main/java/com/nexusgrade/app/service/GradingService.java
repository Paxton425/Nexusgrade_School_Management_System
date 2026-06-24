package com.nexusgrade.app.service;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.model.Result.Term;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GradingService {
    public double calculateMaxGrade(List<Result> grades){
        double max = grades.stream()
                .filter(a -> a.getAssessment().getType() == Assessment.AssessmentType.SBA)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .max().orElse(0.0);
        return max;
    }
    public double calculateMinGrade(List<Result> grades){
        double min = grades.stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.SBA)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .min().orElse(0.0);
        return min;
    }
    public double calculateTermAverage(List<Result> grades, Term term){
        double average = grades.stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.SBA && g.getTerm() == term)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);
        return  average;
    }
    public double calculateResultsAverage(List<Result> results){
        double average = results.stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.SBA)
                .mapToDouble(s -> computeMark(s.getScore(), s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);
        return  average;
    }
    public double calculateFinalMark(List<Result> grades, Subject subject, Term term) {
        double sba = grades.stream()
                .filter(g -> g.getAssessment().getType().equals(Assessment.AssessmentType.SBA) && g.getAssessment().getSubject().equals(subject) && g.getTerm().equals(term))
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);

        double exam = grades.stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.EXAM && g.getTerm() == term)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .findFirst().orElse(0.0);

        // Apply SA FET Phase Weighting: 25% SBA, 75% Exam
        return (sba * 0.25) + (exam * 0.75);
    }

    public Map<String, Number> calculateTermResult(List<Result> grades, Subject subject, Term term) {
        List<Result> filteredResults = grades.stream()
                .filter(g -> g.getAssessment().getType().equals(Assessment.AssessmentType.SBA)
                        && g.getAssessment().getSubject().equals(subject)
                        && g.getTerm().equals(term))
                .toList();

        double results = computeFinal(filteredResults);
        double average = filteredResults.stream().mapToDouble(s -> computeMark(s.getScore(), s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);
        int level = calculateLevel(results);

        return Map.of(
                "results", results,
                "average", average,
                "level", level
                );
    }

    private double computeFinal(List<Result> filteredResults){
        double sba = filteredResults.stream()
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);

        double exam = filteredResults.stream()
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .findFirst().orElse(0.0);

        // Apply SA FET Phase Weighting: 25% SBA, 75% Exam
        return (sba * 0.25) + (exam * 0.75);
    }

    public boolean hasPassedTerm(Student student, Term term) {
        List<Result> grades = student.getResults();
        List<Subject> sutudentSubjects = student.getSchoolClass().getSubjects();
        long passCount = 0;

        if (grades == null || grades.isEmpty()) {
            return false;
        }
        for(Subject subject : sutudentSubjects) {
            double finalMark = calculateFinalMark(grades, subject, term);
            // In SA schooling (FET), a mark below 30% (Level 1) is a fail for that subject
            if (finalMark < 30.0) {
                passCount++;
            }
        }

        // If they cleared 30% in 6/7 subjects, they pass the term
        if(sutudentSubjects.size() == passCount) return true;
        else return false;
    }

    public int calculateLevel(double mark) {
        if (mark >= 80) return 7;
        if (mark >= 70) return 6;
        if(mark >= 60) return 5;
        if(mark >= 50) return 4;
        if(mark >= 40) return 3;
        if(mark >= 30) return 2;
        return 1;
    }

    public double computeMark(Integer score, Integer maxPoints){
        if (maxPoints == 0) return 0.0; // Avoid division by zero
        return (score / (double) maxPoints) * 100; //Mark%
    }
}
