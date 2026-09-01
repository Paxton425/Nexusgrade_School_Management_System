package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.AcademicCalendar;
import com.nexusgrade.app.model.AssessmentScore;
import com.nexusgrade.app.model.Student;

import java.time.LocalDateTime;
import java.util.List;

public class AssessmentScoreDTO {
    private Long Id;
    private Integer score;
    private AssessmentDTO assessment;
    private AcademicCalendarDTO academicCalendar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private StudentDTO student;

    public AssessmentScoreDTO(){}

    public AssessmentScoreDTO(AssessmentScore score){
        this.Id = score.getId();
        this.score = score.getScore();
        this.assessment = new AssessmentDTO(score.getAssessment());
        this.student = StudentDTO.getEssentialsOnly(score.getStudent()); //sets id & names only
        //this.academicCalendar = new AcademicCalendarDTO(score.getAcademicCalendar());
        this.createdAt = score.getCreatedAt();
        this.updatedAt = score.getUpdatedAt();
    }

    public static List<AssessmentScoreDTO> toDTOList(List<AssessmentScore> scores){
        List<AssessmentScoreDTO> dtoAssessmentScores = scores.stream().map(r -> new AssessmentScoreDTO(r)).toList();
        return dtoAssessmentScores;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public AssessmentDTO getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentDTO assessment) {
        this.assessment = assessment;
    }

    public AcademicCalendarDTO getAcademicCalendar() {
        return academicCalendar;
    }

    public void setAcademicCalendar(AcademicCalendar academicCalendar) {
        this.academicCalendar = new AcademicCalendarDTO(academicCalendar);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = new StudentDTO(student);
    }
}
