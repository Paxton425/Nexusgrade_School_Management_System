package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, UUID> {
    TimeTable findFirstBySchoolClass(List<SchoolClass> schoolClass);

    @Query("SELECT COUNT(tt.timeTablePeriods) FROM TimeTable tt WHERE tt.id = :timeTableId")
    Integer getTotalTimetablePeriods(@Param("timeTableId") UUID timeTableId);

    @Query("SELECT COUNT(DISTINCT tp.subject) FROM TimeTablePeriod tp WHERE tp.classTimeTable.id=:timeTableId")
    Integer getTimeTableSubjectsCount(@Param("timeTableId") UUID timeTableId);
}
