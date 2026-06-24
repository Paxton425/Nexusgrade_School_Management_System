package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    @Query(value = "SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<ActivityLog> findRecentActivities(@Param("limit") int limit);
}
