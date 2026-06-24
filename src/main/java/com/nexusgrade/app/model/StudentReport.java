package com.nexusgrade.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class StudentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double overallTotal = 0.0;
    private double overallAverage = 0.0;
    private double highestMark = 0.0;
    private double lowestMark = 0.0;
    @ManyToOne
    private Student student;
    @OneToMany(mappedBy = "studentReport", cascade = CascadeType.ALL)
    private List<StudentTermReport> termResults;

    public StudentReport(){}
    public StudentReport(@Nullable Long id,
                         double overallTotal,
                         double overallAverage,
                         double highestMark,
                         double lowestMark,
                         Student student,
                         List<StudentTermReport> termResults) {
        this.id = id;
        this.overallTotal = overallTotal;
        this.overallAverage = overallAverage;
        this.highestMark = highestMark;
        this.lowestMark = lowestMark;
        this.student = student;
        this.termResults = termResults;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getOverallTotal() {
        return overallTotal;
    }

    public void setOverallTotal(double overallTotal) {
        this.overallTotal = overallTotal;
    }

    public double getOverallAverage() {
        return overallAverage;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<StudentTermReport> getTermResults() {
        return termResults;
    }

    public void setTermResults(List<StudentTermReport> termResults) {
        this.termResults = termResults;
    }
}
