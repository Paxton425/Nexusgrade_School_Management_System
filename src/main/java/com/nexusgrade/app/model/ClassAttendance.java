package com.nexusgrade.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "class_attendance", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "class_session_id"})
})
public class ClassAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    Student student;
    @ManyToOne
    ClassSession classSession;
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;  // PRESENT, ABSENT, LATE, EXCUSED, LEFT_EARLY
    @Column(columnDefinition = "TEXT")
    private String remarks;  // For any comment: "Sick", "Late due to traffic", etc.
    @ManyToOne
    @JoinColumn(name = "marked_by")
    private Instructor markedBy;
    @Column(name = "excuse_reason", columnDefinition = "TEXT")
    private String excuseReason;  // ONLY populated if status = EXCUSED
    @Column(name = "excuse_attachment_url")
    private String excuseAttachmentUrl;  // Parent uploads doctor's note
    @Column(name = "is_excused")
    private Boolean excused = false;  // Was the absence officially excused?
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum AttendanceStatus {
        PRESENT(false, "#4CAF50"),
        ABSENT(true, "#F44336"),
        LATE(false, "#FF9800"),
        EXCUSED(true, "#2196F3"),
        LEFT_EARLY(false, "#9E9E9E");

        private final boolean isAbsent;
        private final String color;

        AttendanceStatus(boolean isAbsent, String color) {
            this.isAbsent = isAbsent;
            this.color = color;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassSession getClassSession() {
        return classSession;
    }

    public void setClassSession(ClassSession classSession) {
        this.classSession = classSession;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Instructor getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(Instructor markedBy) {
        this.markedBy = markedBy;
    }

    public String getExcuseReason() {
        return excuseReason;
    }

    public void setExcuseReason(String excuseReason) {
        this.excuseReason = excuseReason;
    }

    public String getExcuseAttachmentUrl() {
        return excuseAttachmentUrl;
    }

    public void setExcuseAttachmentUrl(String excuseAttachmentUrl) {
        this.excuseAttachmentUrl = excuseAttachmentUrl;
    }

    public Boolean getExcused() {
        return excused;
    }

    public void setExcused(Boolean excused) {
        this.excused = excused;
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
