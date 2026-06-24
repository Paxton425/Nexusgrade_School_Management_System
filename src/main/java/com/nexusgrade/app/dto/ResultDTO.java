package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Result.Term;

import java.time.LocalDateTime;
import java.util.List;

public class ResultDTO {
    Long Id;
    private Integer score;
    private AssessmentDTO assessment;
    private Term term;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private StudentDTO student;

    public ResultDTO(){}

    public ResultDTO(Result result){
        this.Id = result.getId();
        this.score = result.getScore();
        this.assessment = new AssessmentDTO(result.getAssessment());
        this.student = StudentDTO.getEssentialsOnly(result.getStudent()); //sets id & names only
        this.term = result.getTerm();
        this.createdAt = result.getCreatedAt();
        this.updatedAt = result.getUpdatedAt();
    }

    public static List<ResultDTO> toDTOList(List<Result> results){
        List<ResultDTO> dtoResults = results.stream().map(r -> new ResultDTO(r)).toList();
        return dtoResults;
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

    public void setAssessment(Assessment assessment) {
        this.assessment = new AssessmentDTO(assessment);
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
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
