package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.model.Result.Term;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Student;

import java.util.HashMap;
import java.util.TreeMap;

public class StudentReportDTO {
    private StudentDTO student;
    private double overallTotal;
    private double overallAverage;
    private double highestMark;
    private double lowestMark;
    private HashMap<Term, Double> totalsPerTerm = new HashMap<>();
    private HashMap<Term, Double> averagePerTerm = new HashMap<>();
    private TreeMap<String, TreeMap<Term, TermResult>> results;

   public static class TermResult {
       public double finalGrade;
       public double average;
       public Integer gradeLevel;

       public TermResult(double finalGrade, double average, Integer gradeLevel) {
           this.finalGrade = finalGrade;
           this.average = average;
           this.gradeLevel = gradeLevel;
       }
   }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = new StudentDTO(student);
    }

    public double getOverallAverage() {
        return overallAverage;
    }

    public double getOverallTotal() {
        return overallTotal;
    }

    public void setOverallTotal(double overallTotal) {
        this.overallTotal = overallTotal;
    }

    public void setOverallAverage(double overallAverage) {
        this.overallAverage = overallAverage;
    }

    public double getHighestMark() {
        return highestMark;
    }

    public void setHighestMark(double highestMark) {
        this.highestMark = highestMark;
    }

    public double getLowestMark() {
        return lowestMark;
    }

    public void setLowestMark(double lowestMark) {
        this.lowestMark = lowestMark;
    }

    public TreeMap<String, TreeMap<Result.Term, TermResult>> getResults() {
        return results;
    }

    public HashMap<Term, Double> getTotalsPerTerm() {
        return totalsPerTerm;
    }

    public void setTotalsPerTerm(HashMap<Term, Double> totalsPerTerm) {
        this.totalsPerTerm = totalsPerTerm;
    }

    public HashMap<Term, Double> getAveragePerTerm() {
        return averagePerTerm;
    }

    public void setAveragePerTerm(HashMap<Term, Double> averagePerTerm) {
        this.averagePerTerm = averagePerTerm;
    }

    public void setResults(TreeMap<String, TreeMap<Result.Term, TermResult>> results) {
        this.results = results;
    }
}
