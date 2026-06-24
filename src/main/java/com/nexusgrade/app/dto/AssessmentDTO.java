package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.Assessment.AssessmentType;
import com.nexusgrade.app.model.SchoolClass;

import java.time.LocalDate;
import java.util.List;

public class AssessmentDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate assignmentIssueDate;
    private LocalDate assignmentDeadline;
    private Integer maxPoints;
    private AssessmentType type; // SBA, PAT, EXAM
    private SubjectDTO subject;
    private List<SchoolClass> schoolClasses;

    public AssessmentDTO(Assessment assessment){
        this.id = assessment.getId();
        this.title = assessment.getTitle();
        this.description = assessment.getDescription();
        this.assignmentIssueDate = assessment.getAssignmentIssueDate();
        this.assignmentDeadline = assessment.getAssignmentDeadline();
        this.maxPoints = assessment.getMaxPoints();
        this.type = assessment.getType();
        this.subject = new SubjectDTO(assessment.getSubject());
    }

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

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}
