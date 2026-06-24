package com.nexusgrade.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @OneToMany(mappedBy = "classTimeTable")
    List<SchoolClass> schoolClass;
    @OneToMany(mappedBy = "classTimeTable")
    List<TimeTablePeriod> timeTablePeriods;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<SchoolClass> getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(List<SchoolClass> schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<TimeTablePeriod> getTimeTablePeriods() {
        return timeTablePeriods;
    }

    public void setTimeTablePeriods(List<TimeTablePeriod> timeTablePeriods) {
        this.timeTablePeriods = timeTablePeriods;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
