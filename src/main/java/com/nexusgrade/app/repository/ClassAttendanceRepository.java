package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.ClassAttendance;
import com.nexusgrade.app.model.ClassSession;
import com.nexusgrade.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassAttendanceRepository extends JpaRepository<ClassAttendance, Long> {

    List<ClassAttendance> findClassAttendancesByClassSession(ClassSession classSession);

    List<ClassAttendance> findClassAttendancesByStudent(Student student);

    // Get all attendance for a session
    List<ClassAttendance> findByClassSessionId(UUID sessionId);

    // Get attendance for a student with date range
    List<ClassAttendance> findByStudentIdAndClassSessionSessionDateBetween(
            UUID studentId,
            LocalDate startDate,
            LocalDate endDate
    );

    // Get attendance for a student in a specific class
    List<ClassAttendance> findByStudentIdAndClassSessionSchoolClassId(
            UUID studentId,
            Long classId
    );

    // Get attendance summary for a session
    @Query("SELECT ca.status, COUNT(ca) FROM ClassAttendance ca " +
            "WHERE ca.classSession.id = :sessionId " +
            "GROUP BY ca.status")
    List<Object[]> countByStatusForSession(@Param("sessionId") UUID sessionId);

    // Check if attendance already marked
    boolean existsByClassSessionIdAndStudentId(UUID sessionId, UUID studentId);

    // Get students marked present for a session
    @Query("SELECT ca.student.id FROM ClassAttendance ca " +
            "WHERE ca.classSession.id = :sessionId " +
            "AND ca.status = :status")
    List<UUID> findStudentIdsBySessionAndStatus(
            @Param("sessionId") UUID sessionId,
            @Param("status") ClassAttendance.AttendanceStatus status
    );

    @Query("""
        SELECT ClassAttendance FROM ClassAttendance ca
            JOIN ca.classSession cs
            JOIN cs.schoolClass sc
            WHERE sc.id=:classId AND cs.sessionDate=:date
    """)
    List<ClassAttendance> getClassAttendanceByDate(@Param("classId") Long classId, @Param("date") LocalDate date);

    @Query("""
        SELECT 
            COALESCE(ROUND(COUNT(CASE WHEN ca.status IN ('PRESENT', 'LATE') THEN 1 END) * 100.0 / COUNT(ca), 2), 0)
            FROM ClassAttendance ca
            JOIN ca.classSession cs
            JOIN cs.schoolClass sc
            WHERE sc.id = :classId 
            AND cs.sessionDate = :date
    """)
    Double getAttendancePercentByClassIdAndDate(@Param("classId") Long classId, @Param("date") LocalDate date);

    // Get attendance percentage for a student
    @Query("""
        SELECT 
            COALESCE(ROUND(COUNT(CASE WHEN ca.status = 'PRESENT' OR ca.status = 'LATE' THEN 1 END) * 100.0 / COUNT(ca), 2), 0)
            FROM ClassAttendance ca 
            WHERE ca.student.id = :studentId 
            AND ca.classSession.sessionDate BETWEEN :startDate AND :endDate
    """)
    Double calculateAttendancePercentage(
            @Param("studentId") UUID studentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
            "COUNT(CASE WHEN ca.status = 'PRESENT' OR ca.status = 'LATE' THEN 1 END) * 100.0 / COUNT(ca) " +
            "FROM ClassAttendance ca " +
            "WHERE ca.student.id = :studentId " +
            "AND ca.classSession.sessionDate BETWEEN :startDate AND :endDate")
    Double calculateTermAttendancePercentage(
            @Param("studentId") UUID studentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
