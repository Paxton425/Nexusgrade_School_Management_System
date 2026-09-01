package com.nexusgrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer grade;
    @Column(nullable = false)
    private Integer classYear;
    @ManyToOne
    private Instructor classTeacher;
    @ManyToMany(mappedBy = "assignedClasses")
    @JsonIgnoreProperties("assignedClasses")
    private List<Instructor> instructors;
    @OneToMany(mappedBy = "schoolClass")
    @JsonIgnoreProperties("schoolClass")
    private List<Student> students;
    @ManyToMany
    @JoinTable(
            name = "class_subjects",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "class_time_table_id", referencedColumnName = "id")
    TimeTable classTimeTable;
    @ManyToMany(mappedBy = "schoolClasses")
    private List<Assessment> assessments;
    @OneToMany(mappedBy = "schoolClass")
    private List<ClassSession> classSessions;


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

    public Instructor getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(Instructor classTeacher) {
        this.classTeacher = classTeacher;
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public TimeTable getClassTimeTable() {
        return classTimeTable;
    }

    public void setClassTimeTable(TimeTable classTimeTable) {
        this.classTimeTable = classTimeTable;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<ClassSession> getClassSessions() {
        return classSessions;
    }

    public void setClassSessions(List<ClassSession> classSessions) {
        this.classSessions = classSessions;
    }
}
