package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Instructor;
import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Student;

import java.util.ArrayList;
import java.util.List;

public class SchoolClassDTO {
    private Long id;
    private String title;
    private Integer grade;
    private Integer classYear;
    private List<Instructor> instructors;
    private List<Student> students;

    public SchoolClassDTO(SchoolClass schoolClass) {
        this.id = schoolClass.getId();
        this.title = schoolClass.getTitle();
        this.grade = schoolClass.getGrade();
        this.classYear = schoolClass.getClassYear();
        //this.instructors = schoolClass.getInstructors();
        //this.instructors = schoolClass.getInstructors();
        //this.students = schoolClass.getStudents();
    }

    public static List<SchoolClassDTO> ListOf(List<SchoolClass> schoolClasses){
        List<SchoolClassDTO> cDTOs = schoolClasses.stream()
                .map(c-> new SchoolClassDTO(c)).toList();
        return cDTOs;
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getClassYear() {
        return classYear;
    }

    public void setClassYear(Integer classYear) {
        this.classYear = classYear;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
