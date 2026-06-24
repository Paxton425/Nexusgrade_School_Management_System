package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.SchoolClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findAssessmentBySchoolClasses(List<SchoolClass> schoolClasses);

    @Query("SELECT a FROM Assessment a LEFT JOIN a.subject s WHERE " +
            "(:search IS NULL OR :search = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:subjectName IS NULL OR :subjectName = '' OR LOWER(s.name) = LOWER(:subjectName)) AND " +
            "(:type IS NULL OR :type = '' OR a.type = :type)")
    Page<Assessment> findWithFilters(
            @Param("search") String search,
            @Param("subjectName") String subjectName,
            @Param("type") Assessment.AssessmentType type,
            Pageable pageable);
}
