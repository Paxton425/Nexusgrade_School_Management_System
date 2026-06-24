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
    private String performedBy; // The user who performed the action (Teacher/Admin)
    private String action; // The type of action (e.g., "CREATE", "UPDATE", "DELETE", "LOGIN")
    private String entityType; // The category (e.g., "ASSESSMENT", "STUDENT", "GRADE")
    private String entityId;

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
    public ActivityLog(String performedBy, String action, String entityType, String entityId, String details) {
        this.performedBy = performedBy;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
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

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
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
