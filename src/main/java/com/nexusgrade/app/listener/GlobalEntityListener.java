package com.nexusgrade.app.listener;

import com.nexusgrade.app.event.EntityUpdatedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class GlobalEntityListener implements ApplicationEventPublisherAware {

    private static ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void onEntityChange(Object entity) {
        if (eventPublisher != null) {
            // Broadcasts that an entity changed (e.g., "Student", "Result")
            eventPublisher.publishEvent(new EntityUpdatedEvent(entity.getClass().getSimpleName()));
        }
    }
}