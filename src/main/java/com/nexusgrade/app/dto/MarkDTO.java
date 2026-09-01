package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.AssessmentMark;

import java.time.LocalDateTime;

public class MarkDTO {
    private Long id;
    private Double mark;
    private AssessmentScoreDTO scoreDTO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MarkDTO(AssessmentMark assessmentMark) {
        this.id = assessmentMark.getId();
        this.mark = assessmentMark.getMark();
        this.scoreDTO = new AssessmentScoreDTO(assessmentMark.getAssessmentScore());
        this.createdAt = assessmentMark.getCreatedAt();
        this.updatedAt = assessmentMark.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public AssessmentScoreDTO getScoreDTO() {
        return scoreDTO;
    }

    public void setScoreDTO(AssessmentScoreDTO scoreDTO) {
        this.scoreDTO = scoreDTO;
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
}
