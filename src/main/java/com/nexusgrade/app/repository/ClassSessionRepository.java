package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

    // ===== FIND LAST SESSION =====

    // 1. Last session overall
    @Query("SELECT cs FROM ClassSession cs ORDER BY cs.sessionDate DESC, cs.startTime DESC LIMIT 1")
    Optional<ClassSession> findLastSession();

    // 2. Last session for a specific class
    @Query("SELECT cs FROM ClassSession cs WHERE cs.schoolClass.id = :classId ORDER BY cs.sessionDate DESC, cs.startTime DESC LIMIT 1")
    Optional<ClassSession> findLastSessionByClassId(@Param("classId") Long classId);

    // 3. Last session for a specific teacher
    @Query("SELECT cs FROM ClassSession cs WHERE cs.instructor.id = :instructorId ORDER BY cs.sessionDate DESC, cs.startTime DESC LIMIT 1")
    Optional<ClassSession> findLastSessionByInstructorId(@Param("instructorId") Long instructorId);

    // 4. Last session for a specific subject
    @Query("SELECT cs FROM ClassSession cs WHERE cs.subject.id = :subjectId ORDER BY cs.sessionDate DESC, cs.startTime DESC LIMIT 1")
    Optional<ClassSession> findLastSessionBySubjectId(@Param("subjectId") Long subjectId);

    // 5. Last session before a specific date
    @Query("SELECT cs FROM ClassSession cs WHERE cs.sessionDate < :date AND cs.schoolClass.id = :classId ORDER BY cs.sessionDate DESC, cs.startTime DESC LIMIT 1")
    Optional<ClassSession> findLastSessionBeforeDate(@Param("classId") Long classId, @Param("date") LocalDate date);

    // 6. Last session with attendance already marked
    @Query("SELECT cs FROM ClassSession cs JOIN cs.classAttendances ca WHERE cs.schoolClass.id = :classId ORDER BY cs.sessionDate DESC, cs.startTime DESC LIMIT 1")
    Optional<ClassSession> findLastSessionWithAttendance(@Param("classId") Long classId);

    // ===== FIND BY DATE =====

    // Get all sessions for a class on a specific date
    List<ClassSession> findBySchoolClassIdAndSessionDate(Long classId, LocalDate date);

    // Get all sessions for a class between dates
    List<ClassSession> findBySchoolClassIdAndSessionDateBetween(Long classId, LocalDate start, LocalDate end);

    // ===== FIND TODAY'S SESSIONS =====

    default List<ClassSession> findTodaySessionsByClassId(Long classId) {
        return findBySchoolClassIdAndSessionDate(classId, LocalDate.now());
    }

    default Optional<ClassSession> findMostRecentSessionByClassId(Long classId) {
        return findLastSessionByClassId(classId);
    }
}