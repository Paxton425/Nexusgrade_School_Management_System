package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Report;
import com.nexusgrade.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findStudentReportByStudent_Id(UUID studentId);
}
