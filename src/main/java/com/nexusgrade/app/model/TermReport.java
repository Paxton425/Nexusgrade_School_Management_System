package com.nexusgrade.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
public class TermReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double finalGrade = 0.0;
    private double average = 0.0;
    private Integer gradeLevel = 1;
    @ManyToOne
    private Subject subject;
    @ManyToOne
    private Report report;
    @Enumerated(EnumType.STRING)
    private Term term;

    public TermReport(){}
    public TermReport(@Nullable Long id, double finalGrade, double average, Integer gradeLevel, Subject subject, @Nullable Report report, Term term) {
        this.id = id;
        this.finalGrade = finalGrade;
        this.average = average;
        this.gradeLevel = gradeLevel;
        this.subject = subject;
        this.report = report;
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

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
