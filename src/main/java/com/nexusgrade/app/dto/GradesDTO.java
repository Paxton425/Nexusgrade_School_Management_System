package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.Instructor;

import java.util.List;

public class GradesDTO {
    public GradesDTO(Long id, StudentDTO student, SubjectDTO subject, Instructor instructor, List<ResultDTO> grades){}
    public Long id;
    public StudentDTO student;
    public SubjectDTO subject;
    public Instructor instructor;
    public List<ResultDTO> grades;

    public class ResultDTO{
        Long Id;
        private Double score;
        private Assessment assessment;

    }
}