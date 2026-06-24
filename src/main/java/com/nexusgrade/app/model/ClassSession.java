package com.nexusgrade.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "class_sessions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"school_class_id", "session_date", "start_time"})
})
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTablePeriod timeTablePeriod;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    @ManyToOne(fetch = FetchType.LAZY)
    private Instructor instructor;
    @ManyToOne
    SchoolClass schoolClass;
    @OneToMany(mappedBy = "classSession")
    List<ClassAttendance> classAttendances;
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;
    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rescheduled_from")
    private ClassSession rescheduledFrom;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TimeTablePeriod getTimeTablePeriod() {
        return timeTablePeriod;
    }

    public void setTimeTablePeriod(TimeTablePeriod timeTablePeriod) {
        this.timeTablePeriod = timeTablePeriod;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<ClassAttendance> getClassAttendances() {
        return classAttendances;
    }

    public void setClassAttendances(List<ClassAttendance> classAttendances) {
        this.classAttendances = classAttendances;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public ClassSession getRescheduledFrom() {
        return rescheduledFrom;
    }

    public void setRescheduledFrom(ClassSession rescheduledFrom) {
        this.rescheduledFrom = rescheduledFrom;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
