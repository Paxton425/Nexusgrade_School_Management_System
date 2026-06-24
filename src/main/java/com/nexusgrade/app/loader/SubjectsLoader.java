package com.nexusgrade.app.loader;

import com.nexusgrade.app.repository.*;
import com.nexusgrade.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectsLoader {

    @Autowired
    private StudentRepository studentRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private AssessmentRepository assessmentRepo;
    @Autowired private ResultRepository achievementRepo;
    @Autowired private InstructorRepository teacherRepo;
    @Autowired private ClassRepository classRepository;

    public void loadSubjectStreams(){
    }
}
