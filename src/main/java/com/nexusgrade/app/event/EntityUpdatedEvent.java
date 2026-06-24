package com.nexusgrade.app.event;

public class EntityUpdatedEvent {
    private final String entityName;

    public EntityUpdatedEvent(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }
}
