package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<StudentReport, Long> {
    Optional<StudentReport> findStudentReportByStudent_Id(UUID studentId);
}
