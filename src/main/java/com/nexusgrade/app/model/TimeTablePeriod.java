package com.nexusgrade.app.model;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
public class TimeTablePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private SessionType sessionType; //e.g. CLASS, BREAK, LUNCH, FREE_PERIOD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;
    @ManyToOne
    private Instructor instructor;
    @ManyToOne
    TimeTable classTimeTable;
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 1=Monday
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Column(name = "is_active")
    private Boolean isActive = false;
    @Enumerated(EnumType.STRING)
    private PeriodColor color;

    public static enum PeriodColor {
        BLUE("#3B82F6"),
        GREEN("#22C55E"),
        RED("#EF4444"),
        PURPLE("#A855F7"),
        ORANGE("#F97316"),
        TEAL("#14B8A6"),
        PINK("#EC4899"),
        INDIGO("#6366F1"),
        YELLOW("#EAB308"),
        CYAN("#06B6D4");

        private final String hexCode;

        PeriodColor(String hexCode) {
            this.hexCode = hexCode;
        }

        public String getHexCode() {
            return hexCode;
        }
    }

    public enum SessionType { CLASS, BREAK, LUNCH, FREE_PERIOD }

    @PreUpdate
    private void validateSessionType() {
        if (sessionType == SessionType.CLASS) {
            if (classTimeTable == null) {
                throw new IllegalArgumentException("Class Time Table cannot be null for CLASS session type");
            }
            if (subject == null) {
                throw new IllegalArgumentException("Subject cannot be null for CLASS session type");
            }
        }

        if (sessionType != SessionType.CLASS) {
            if (subject != null) {
                throw new IllegalArgumentException("Subject not needed type");
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public TimeTable getClassTimeTable() {
        return classTimeTable;
    }

    public void setClassTimeTable(TimeTable classTimeTable) {
        this.classTimeTable = classTimeTable;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public PeriodColor getColor() {
        return color;
    }

    public void setColor(PeriodColor color) {
        this.color = color;
    }
}
