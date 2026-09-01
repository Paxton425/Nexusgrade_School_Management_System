package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Instructor;

import java.util.List;

public class GradesDTO {
    private Long id;
    private StudentDTO student;
    private SubjectDTO subject;
    private Instructor instructor;
    private List<AssessmentScoreDTO> scores;

    public GradesDTO(Long id, StudentDTO student, SubjectDTO subject, Instructor instructor, List<AssessmentScoreDTO> scores) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.instructor = instructor;
        this.scores = scores;
    }
}