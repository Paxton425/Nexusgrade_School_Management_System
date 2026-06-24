package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.model.Result.Term;
import com.nexusgrade.app.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByAssessment(Assessment assessment);

    // Instantly gets counts and averages at database level without loading entities
    @Query("SELECT COUNT(r) FROM Result r")
    long countSubmissions();

    @Query("SELECT COUNT(r) FROM Result r WHERE r.score >= ?1")
    long countAllByOrLessThan(Double score);

    @Query("SELECT AVG(r.score) FROM Result r")
    Double getAverageScore();

    @Query("SELECT AVG(r.score) FROM Result r WHERE r.assessment.subject = ?1")
    Double getSubjectAverage(Subject subject);

    // Pulls EXACTLY the top 5 records using SQL LIMIT, instead of sorting a massive loop in Java
    @Query(value = "SELECT * FROM result ORDER BY score DESC LIMIT 5", nativeQuery = true)
    List<Result> findTop5ByOrderByScoreDesc();

    // Let MySQL calculate the total scores per grade grouping instantly
    @Query("SELECT r.student.schoolClass.grade, AVG(r.score) FROM Result r GROUP BY r.student.schoolClass.grade")
    List<Object[]> getAverageScorePerGrade();

    @Query("SELECT COALESCE(SUM(a.maxPoints), 0) " +
            "FROM Assessment a " +
            "WHERE a.subject.grade = :grade")
    Long getTotalMaxScoreForGrade(@Param("grade") int grade);

    @Query("SELECT " +
            "    r.assessment.subject.subjectCode AS subjectCode, " +

            "    ROUND( " +
            "        (SUM(CASE WHEN r.term = :term1 THEN r.score ELSE 0 END) * 100.0) " +
            "        / :totalScore , 2) AS term1Percentage, " +

            "    ROUND( " +
            "        (SUM(CASE WHEN r.term = :term2 THEN r.score ELSE 0 END) * 100.0) " +
            "        / :totalScore , 2) AS term2Percentage " +

            "FROM Result r " +
            "WHERE r.term IN (:term1, :term2) " +
            "  AND r.student.schoolClass.grade = :grade " +
            "GROUP BY r.assessment.subject.subjectCode " +
            "ORDER BY r.assessment.subject.subjectCode")
    List<Object[]> getSubjectPerformancePercentages(
            @Param("term1") Term term1,
            @Param("term2") Term term2,
            @Param("grade") int grade,
            @Param("totalScore") long totalScore);   // ← Passed from outside
}
