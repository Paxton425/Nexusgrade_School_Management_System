package com.nexusgrade.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;

/*
* Derived data, for performance
* Reduces retrieval time instead of calculating for each request
*/

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "grade_level", "academic_year"})
})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double overallTotal = 0.0;
    private double overallAverage = 0.0;
    private double highestMark = 0.0;
    private double lowestMark = 0.0;
    @ManyToOne
    private Student student;
    @Column(name = "grade_level")
    private Integer gradeLevel;
    @Column(name = "academic_year")
    private Integer academicYear;
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<TermReport> termResults;

    public Report(){}
    public Report(@Nullable Long id,
                         double overallTotal,
                         double overallAverage,
                         double highestMark,
                         double lowestMark,
                         Student student,
                         Integer gradeLevel,
                         Integer academicYear,
                         List<TermReport> termResults) {
        this.id = id;
        this.overallTotal = overallTotal;
        this.overallAverage = overallAverage;
        this.highestMark = highestMark;
        this.lowestMark = lowestMark;
        this.student = student;
        this.gradeLevel = gradeLevel;
        this.academicYear = academicYear;
        this.termResults = termResults;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Integer gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public List<TermReport> getTermResults() {
        return termResults;
    }

    public void setTermResults(List<TermReport> termResults) {
        this.termResults = termResults;
    }
}
