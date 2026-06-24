package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.ClassAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<ClassAttendance, Long> {
}
