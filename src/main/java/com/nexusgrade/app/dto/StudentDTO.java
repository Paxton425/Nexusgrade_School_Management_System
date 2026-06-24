package com.nexusgrade.app.dto;
import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Student.Status;
import com.nexusgrade.app.model.Student.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class StudentDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Status status;
    private LocalDate birthDay;
    private List<SubjectDTO> subjects;
    private List<ResultDTO> results;
    private SchoolClassDTO schoolClassDTO;

    public StudentDTO(){}
    public StudentDTO(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.gender = student.getGender();
        this.birthDay = student.getBirthDay();
        this.status = student.getStatus();
        this.results = ResultDTO.toDTOList(student.getResults());
        this.schoolClassDTO = new SchoolClassDTO(student.getSchoolClass());
    }

    public static StudentDTO getEssentialsOnly(Student student){
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SchoolClassDTO getSchoolClassDTO() {
        return schoolClassDTO;
    }

    public void setSchoolClassDTO(SchoolClassDTO schoolClassDTO) {
        this.schoolClassDTO = schoolClassDTO;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public List<SubjectDTO> getSubjects() {
        return subjects;
    }

    public List<ResultDTO> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = ResultDTO.toDTOList(results);
    }

    public void setSubjects(List<SubjectDTO> enrollmentSubjects) {
        this.subjects = enrollmentSubjects;
    }
}
