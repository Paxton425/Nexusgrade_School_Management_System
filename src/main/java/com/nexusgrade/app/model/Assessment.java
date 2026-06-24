package com.nexusgrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDate assignmentIssueDate;
    private LocalDate assignmentDeadline;
    private Integer maxPoints;
    @Enumerated(EnumType.STRING)
    private AssessmentType type; // SBA, PAT, EXAM
    @ManyToOne
    @JsonIgnoreProperties("assessments")
    private Subject subject;
    @ManyToMany
    @JoinTable(
            name = "class_assessment",
            joinColumns = @JoinColumn(name = "assessment_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private List<SchoolClass> schoolClasses;

    public enum AssessmentType { SBA, PAT, EXAM }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAssignmentIssueDate() {
        return assignmentIssueDate;
    }

    public void setAssignmentIssueDate(LocalDate assignmentIssueDate) {
        this.assignmentIssueDate = assignmentIssueDate;
    }

    public LocalDate getAssignmentDeadline() {
        return assignmentDeadline;
    }

    public void setAssignmentDeadline(LocalDate assignmentDeadline) {
        this.assignmentDeadline = assignmentDeadline;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}
