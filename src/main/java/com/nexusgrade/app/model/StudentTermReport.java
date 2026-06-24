package com.nexusgrade.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
public class StudentTermReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double finalGrade = 0.0;
    private double average = 0.0;
    private Integer gradeLevel = 1;
    @ManyToOne
    private Subject subject;
    @ManyToOne
    private StudentReport studentReport;
    @Enumerated(EnumType.STRING)
    private Result.Term term;

    public StudentTermReport(){}
    public StudentTermReport(@Nullable Long id, double finalGrade, double average, Integer gradeLevel, Subject subject, @Nullable StudentReport studentReport, Result.Term term) {
        this.id = id;
        this.finalGrade = finalGrade;
        this.average = average;
        this.gradeLevel = gradeLevel;
        this.subject = subject;
        this.studentReport = studentReport;
        this.term = term;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Integer gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public StudentReport getStudentReport() {
        return studentReport;
    }

    public void setStudentReport(StudentReport studentReport) {
        this.studentReport = studentReport;
    }

    public Result.Term getTerm() {
        return term;
    }

    public void setTerm(Result.Term term) {
        this.term = term;
    }
}
