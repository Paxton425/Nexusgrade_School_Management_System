package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.AcademicCalendar;
import com.nexusgrade.app.model.Stats;

import java.time.LocalDateTime;

public class StatsDTO {
    private Long id;
    private Integer studentCount;
    private Integer instructorCount;
    private Double attendanceRate;
    private Double overallAverage;
    private Double passRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AcademicCalendar academicCalendar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Integer getInstructorCount() {
        return instructorCount;
    }

    public void setInstructorCount(Integer instructorCount) {
        this.instructorCount = instructorCount;
    }

    public StatsDTO() {}

    public StatsDTO(Stats stats) {
        this.id = id;
        this.studentCount = stats.getStudentCount();;
        this.instructorCount = stats.getInstructorCount();
        this.attendanceRate = stats.getAttendanceRate();
        this.passRate = stats.getPassRate();
        this.academicCalendar = stats.getAcademicCalendar();
    }

    public StatsDTO(Long id, Integer studentCount, Integer instructorCount, Double attendanceRate, Double passRate, AcademicCalendar academicCalendar) {
        this.id = id;
        this.studentCount = studentCount;
        this.instructorCount = instructorCount;
        this.attendanceRate = attendanceRate;
        this.passRate = passRate;
        this.academicCalendar = academicCalendar;
    }

    public Double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(Double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public Double getOverallAverage() {
        return overallAverage;
    }

    public void setOverallAverage(Double overallAverage) {
        this.overallAverage = overallAverage;
    }

    public Double getPassRate() {
        return passRate;
    }

    public void setPassRate(Double passRate) {
        this.passRate = passRate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AcademicCalendar getAcademicCalendar() {
        return academicCalendar;
    }

    public void setAcademicCalendar(AcademicCalendar academicCalendar) {
        this.academicCalendar = academicCalendar;
    }
}
