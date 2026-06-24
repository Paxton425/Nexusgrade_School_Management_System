package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.TermReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermReportRepository extends JpaRepository<TermReport, Long> {
}
