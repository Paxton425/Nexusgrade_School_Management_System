package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.AssessmentScore;
import com.nexusgrade.app.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AssessmentScoreRepository extends JpaRepository<AssessmentScore, Long> {
    List<AssessmentScore> findByAssessment(Assessment assessment);

    // Instantly gets counts and averages at database level without loading entities
    @Query("SELECT COUNT(r) FROM AssessmentScore r")
    long countSubmissions();

    @Query("SELECT COUNT(r) FROM AssessmentScore r WHERE r.score >= ?1")
    long countAllByOrLessThan(Double score);

    @Query(value = "SELECT ROUND(AVG((r.score/r.assessment.maxPoints)*100), 2) FROM AssessmentScore r")
    Double getOverallAverageMark();

    @Query("SELECT AVG(r.score) FROM AssessmentScore r")
    Double getAverageScore();

    @Query("SELECT AVG(r.score) FROM AssessmentScore r WHERE r.assessment.subject = ?1")
    Double getSubjectAverage(Subject subject);

    @Query(value = """
        SELECT
        	COUNT(score) as totalAssessmentScores,
        	COUNT(CASE
        		WHEN (score/assessment.max_points)*100 <= 40\s
                THEN 1 END) AS poor,
            COUNT(CASE
        		WHEN (score/assessment.max_points)*100 > 40 AND (score/assessment.max_points)*100 <= 50
                THEN 1 END) AS bad,
        	COUNT(CASE
        		WHEN (score/assessment.max_points)*100 > 50 AND (score/assessment.max_points)*100 <= 60
                THEN 1 END) AS average,
            COUNT(CASE
        		WHEN (score/assessment.max_points)*100 > 60 AND (score/assessment.max_points)*100 <= 70
                THEN 1 END) AS good,
            COUNT(CASE WHEN (score/assessment.max_points)*100 > 70\s
            THEN 1 END) AS excellent
        	FROM assessment_scores
            INNER JOIN assessment ON assessment.id = assessment_scores.assessment_id;
    """, nativeQuery = true)
    Map<String, Object> getPerformanceLevelDistribution();

    // Let MySQL calculate the total scores per grade grouping instantly
    @Query(value = """
        SELECT subject.grade,
            COALESCE(SUM(assessment_scores.score), 0) AS totalScore,
            ROUND(COALESCE(SUM(assessment_scores.score)/SUM(assessment.max_points)*100, 0), 2) AS totalPercentageMark,
            ROUND(COALESCE(AVG((assessment_scores.score/assessment.max_points)*100), 0), 2) AS average
            FROM subject
            LEFT JOIN assessment on assessment.subject_id = subject.id
            LEFT JOIN assessment_scores on assessment_scores.assessment_id = assessment.id
            GROUP BY subject.grade
            ORDER BY subject.grade
    """, nativeQuery = true)
    List<Object[]> getAverageMarkPerGrade();

    @Query(value = """
        WITH grid(grade, term) AS (
            -- 1. Create the master list with explicitly named grid columns
            SELECT DISTINCT s.grade, t.term
            FROM subject s
            CROSS JOIN (
                SELECT :prevTerm AS term -- Added colon prefix and explicit alias
                UNION ALL
                SELECT :currTerm AS term -- Ensured clean naming matching the union
            ) t
        ),
        averages(grade, term, raw_avg) AS (
            -- 2. Explicitly name the calculation columns
            SELECT
                sub.grade,
                cal.term,
                AVG((res.score / asm.max_points) * 100)
            FROM subject sub
            INNER JOIN assessment asm ON asm.subject_id = sub.id
            INNER JOIN assessment_scores res ON res.assessment_id = asm.id
            INNER JOIN academic_calendar cal ON cal.id = res.academic_calendar_id
            WHERE cal.term IN (:prevTerm, :currTerm)
            GROUP BY sub.grade, cal.term
        )
        -- 3. Match them up perfectly using the defined structures
        SELECT
            g.grade,
            g.term,
            ROUND(COALESCE(a.raw_avg, 0), 2) AS averageMark
        FROM grid g
        LEFT JOIN averages a
            ON a.grade = g.grade
            AND a.term = g.term
        ORDER BY
            g.grade,
            g.term
    """, nativeQuery = true)
    List<Object[]> getTermAveragesPerGrade(
            @Param("currTerm")
            String currentTerm,
            @Param("prevTerm")
            String previousTerm);

    @Query("SELECT COALESCE(SUM(a.maxPoints), 0) " +
            "FROM Assessment a " +
            "WHERE a.subject.grade = :grade")
    Long getTotalMaxScoreForGrade(@Param("grade") int grade);

    @Query(value = """
    SELECT
        academic_calendar.term,
        subject.id AS subjectId, 
        subject.subject_code AS subjectCode,
        subject.name, 
        COALESCE(SUM(assessment_scores.score), 0) AS totalScore,
        COALESCE(SUM(assessment.max_points), 0) AS maxTotalScore,
        CASE 
            WHEN COALESCE(SUM(assessment.max_points), 0) = 0 THEN 0
            ELSE ROUND(COALESCE(SUM(assessment_scores.score), 0) / SUM(assessment.max_points) * 100, 2)
        END AS performance
    FROM academic_calendar
    CROSS JOIN subject
    LEFT JOIN assessment_scores ON assessment_scores.academic_calendar_id = academic_calendar.id
    LEFT JOIN assessment ON assessment.id = assessment_scores.assessment_id 
        AND assessment.subject_id = subject.id
    WHERE academic_calendar.term IN (:terms)
      AND subject.grade = :grade
    GROUP BY academic_calendar.term, subject.id, subject.subject_code, subject.name
    ORDER BY academic_calendar.term, subject.name
    """, nativeQuery = true)
    List<Object[]> findSubjectPerformanceByTermsAndGrade(
            @Param("terms") List<String> terms,
            @Param("grade") Integer grade
    );

    List<AssessmentScore> findTop5ByOrderByAssessmentMarkMarkDesc();

    @Query("SELECT ac FROM AssessmentScore ac WHERE ac.assessment.id =:id")
    Optional<AssessmentScore> findByAssessmentMarkId(@Param("id") Long id);
}
