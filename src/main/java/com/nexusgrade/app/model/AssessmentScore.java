package com.nexusgrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "assessment_scores",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"assessment_id", "student_id", "academic_calender_id"}) //Students cant have two marks for same assessment & year
})
public class AssessmentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    private Integer score; //eg. 90/100
    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonIgnore
    private Assessment assessment;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_mark_id", referencedColumnName = "id")
    @JsonIgnoreProperties("assessmentScore")
    private AssessmentMark assessmentMark;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("assessmentScores")
    @JsonIgnore
    AcademicCalendar academicCalendar;

    public Long getId() {
        return Id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public AssessmentMark getAssessmentMark() {
        return assessmentMark;
    }

    public void setAssessmentMark(AssessmentMark assessmentMark) {
        this.assessmentMark = assessmentMark;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AcademicCalendar getAcademicCalendar() {
        return academicCalendar;
    }

    public void setAcademicCalendar(AcademicCalendar academicCalendar) {
        this.academicCalendar = academicCalendar;
    }
}