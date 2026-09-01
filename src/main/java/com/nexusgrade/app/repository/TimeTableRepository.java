package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, UUID> {
    Optional<TimeTable> findBySchoolClass(SchoolClass schoolClass);

    @Query("SELECT SIZE(tt.timeTablePeriods) FROM TimeTable tt WHERE tt.id = :timeTableId")
    Integer getTotalTimetablePeriods(@Param("timeTableId") UUID timeTableId);

    @Query("SELECT COUNT(DISTINCT ttp.subject) FROM TimeTablePeriod ttp WHERE ttp.classTimeTable.id=:timeTableId")
    Integer getTimeTableSubjectsCount(@Param("timeTableId") UUID timeTableId);

    @Query("SELECT ttp.startTime, ttp.endTime FROM TimeTablePeriod ttp WHERE ttp.classTimeTable.id = :timeTableId")
    List<Object[]> getTimeSlots(@Param("timeTableId") UUID timeTableId);
}
