package com.nexusgrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // FIX: UUIDs cannot use IDENTITY strategy
    private UUID id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    private Department department;
    @Enumerated(EnumType.STRING)
    private Title title;
    @ManyToMany
    @JoinTable(
            name = "InstructorClass",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "school_class_id"))
    @JsonIgnoreProperties("instructors")
    private List<SchoolClass> assignedClasses;
    @OneToMany(mappedBy = "classTeacher")
    private List<SchoolClass> administeredClasses; // Classes playing class teacher role
    @OneToMany(mappedBy = "instructor")
    private List<TimeTablePeriod> timeTablePeriods;

    public enum Title { TEACHER, HOD, VICE_PRINCIPAL, PRINCIPAL }

    // --- Getters & Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Title getTitle() { return title; }
    public void setTitle(Title title) { this.title = title; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public List<SchoolClass> getAdministeredClasses() {
        return administeredClasses;
    }

    public void setAdministeredClasses(List<SchoolClass> administeredClasses) {
        this.administeredClasses = administeredClasses;
    }

    public List<SchoolClass> getAssignedClasses() {
        return assignedClasses;
    }

    public void setAssignedClasses(List<SchoolClass> assignedClasses) {
        this.assignedClasses = assignedClasses;
    }

    public List<TimeTablePeriod> getTimeTablePeriods() {
        return timeTablePeriods;
    }

    public void setTimeTablePeriods(List<TimeTablePeriod> timeTablePeriods) {
        this.timeTablePeriods = timeTablePeriods;
    }
}