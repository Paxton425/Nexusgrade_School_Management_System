package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Department;
import com.nexusgrade.app.model.Instructor;
import com.nexusgrade.app.model.Instructor.Title;
import com.nexusgrade.app.model.SchoolClass;

import java.util.List;
import java.util.UUID;

public class InstructorDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String employeeId;
    private Department department;
    private Title title;
    private Gender gender;
    private String email;
    private String phone;
    List<SchoolClass> instructorSchoolClasses;

    public enum Gender { MALE, FEMALE };

    public UUID getId() {
        return id;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<SchoolClass> getInstructorSchoolClasses() {
        return instructorSchoolClasses;
    }

    public void setInstructorSchoolClasses(List<SchoolClass> instructorSchoolClasses) {
        this.instructorSchoolClasses = instructorSchoolClasses;
    }
}
