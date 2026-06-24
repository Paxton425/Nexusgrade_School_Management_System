package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {
    List<AcademicCalendar> findAllByAcademicYear(int academicYear);

    @Query("SELECT t FROM AcademicCalendar t WHERE t.isActive = true")
    Optional<AcademicCalendar> findCurrentTermsCalender();

    AcademicCalendar getAcademicCalendarByAcademicYear(int academicYear);
}
