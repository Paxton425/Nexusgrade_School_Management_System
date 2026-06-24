package com.nexusgrade.app.model;

import com.nexusgrade.app.listener.GlobalEntityListener;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@EntityListeners(GlobalEntityListener.class)
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy; // The user who performed the action
    private String performedByTitle; // (Teacher/Admin)
    private String action; // The type of action (e.g., "CREATE", "UPDATE", "DELETE", "LOGIN")
    private String entityType; // The category (e.g., "ASSESSMENT", "STUDENT", "GRADE")
    private String affectedEntity;

    // Descriptive message or JSON string of what changed
    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime timestamp;

    // Automatically set the timestamp before saving
    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    public ActivityLog() {}
    public ActivityLog(User performedBy, String action, String entityType, String affectedEntity, String details) {
        this.performedBy = performedBy;
        this.action = action;
        this.entityType = entityType;
        this.affectedEntity = affectedEntity;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public String getPerformedByTitle() {
        return performedByTitle;
    }

    public void setPerformedByTitle(String performedByTitle) {
        this.performedByTitle = performedByTitle;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAffectedEntity() {
        return affectedEntity;
    }

    public void setAffectedEntity(String affectedEntity) {
        this.affectedEntity = affectedEntity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
