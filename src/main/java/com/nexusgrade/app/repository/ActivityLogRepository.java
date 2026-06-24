package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findFirst5ByOrderByIdDesc();
}
