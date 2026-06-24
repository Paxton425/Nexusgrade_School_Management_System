package com.nexusgrade.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"assessment_id", "student_id", "academic_calender_id"}) //Students cant have two marks for same assessment & year  
})
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    private Integer score; //eg. 90/100
    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
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