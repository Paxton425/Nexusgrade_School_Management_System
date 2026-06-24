package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.ActivityLog;
import com.nexusgrade.app.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class ActivityLogDTO {

    private Long id;
    private UserDTO performedBy; // The user who performed the action
    private String performedByTitle; // (Teacher/Admin)
    private String action; // The type of action (e.g., "CREATE", "UPDATE", "DELETE", "LOGIN")
    private String entityType; // The category (e.g., "ASSESSMENT", "STUDENT", "GRADE")
    private String effectedEntity;
    private String details;
    private LocalDateTime timestamp;

    public ActivityLogDTO(ActivityLog activityLog){
        this.id = activityLog.getId();
        this.performedBy = new UserDTO(activityLog.getPerformedBy());
        this.performedByTitle = activityLog.getPerformedByTitle();
        this.action = activityLog.getAction();
        this.entityType = activityLog.getEntityType();
        this.effectedEntity = activityLog.getAffectedEntity();
        this.details = activityLog.getDetails();
        this.timestamp = activityLog.getTimestamp();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(UserDTO performedBy) {
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

    public String getEffectedEntity() {
        return effectedEntity;
    }

    public void setEffectedEntity(String effectedEntity) {
        this.effectedEntity = effectedEntity;
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
