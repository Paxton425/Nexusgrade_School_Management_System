package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.TimeTablePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PeriodsRepository extends JpaRepository<TimeTablePeriod, UUID> {
}
