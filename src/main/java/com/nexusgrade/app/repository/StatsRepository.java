package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Stats;
import com.nexusgrade.app.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query(value = """
        select
        	stats.id,
            attendance_rate,
            instructor_count,
            student_count,
            pass_rate,
            overall_average
            term
        from stats
        left join academic_calendar on academic_calendar.id = academic_calendar_id
        where academic_calendar.is_active = 1;
    """, nativeQuery = true)
    public List<Object[]> getCurrentTermStats();

    Optional<Stats> findFirstByAcademicCalendarTermOrderByCreatedAtDesc(Term term);

    Optional<Stats> findFirstById(Long id);

    @Query(value = "SELECT s FROM Stats s WHERE s.id < :id ORDER BY s.id LIMIT 1")
    Optional<Stats> findFirstBefore(@Param("id") Long id);
}
