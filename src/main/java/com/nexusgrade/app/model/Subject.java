package com.nexusgrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String subjectCode; // e.g., MATH10, PHYS12
    @Column(nullable = false)
    private String name; // e.g., Mathematics
    @Column(nullable = false)
    private Department department;
    @Column(nullable = false)
    private Integer grade;
    private Double sbaWeight;  // e.g., 0.25 (25%)
    private Double examWeight; // e.g., 0.75 (75%)
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("subject")
    private List<Assessment> assessments;
    @ManyToMany(mappedBy = "subjects")
    List<SchoolClass> schoolClasses;
    @OneToMany(mappedBy = "subject")
    List<TermReport> termReport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Phase getPhaseByGrade(Integer grade){
        return Phase.getByGrade(grade);
    }

    public Double getSbaWeight() {
        return sbaWeight;
    }

    public void setSbaWeight(Double sbaWeight) {
        this.sbaWeight = sbaWeight;
    }

    public Double getExamWeight() {
        return examWeight;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public void setExamWeight(Double examWeight) {
        this.examWeight = examWeight;
    }

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}

