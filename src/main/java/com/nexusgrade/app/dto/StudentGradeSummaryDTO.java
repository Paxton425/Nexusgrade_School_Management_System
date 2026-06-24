package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.Student;

import java.util.UUID;
import java.util.stream.DoubleStream;

public class StudentGradeSummaryDTO {
    private String firstName;
    private String lastName;
    private Integer grade;
    private double average;
    private double max;
    private double min;
    private UUID id;

    // Constructor that calculates stats from the Student's enrollments
    public StudentGradeSummaryDTO(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.grade = student.getSchoolClass().getGrade();

        var enrollmentsFinalScores = new double[]{0, 0}; //student.getResults().stream()
                //.mapToDouble(e -> new GradingService().calculateFinalMark(e, Result.Term.TERM_1))
                //.toArray();

        if (enrollmentsFinalScores.length > 0) {
            this.average = DoubleStream.of(enrollmentsFinalScores).average().orElse(0.0);
            this.max = DoubleStream.of(enrollmentsFinalScores).max().orElse(0.0);
            this.min = DoubleStream.of(enrollmentsFinalScores).min().orElse(0.0);
        }
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
