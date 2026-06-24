package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.SchoolClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
    SchoolClass findSchoolClassByGradeContainsAndTitleOrderByTitle(Integer grade, String title); //e.g Grade=7,Title=A
    Page<SchoolClass> findByTitleContainingIgnoreCase(String searchValue, Pageable pageable);
    Page<SchoolClass> findByGradeAndTitleContainingIgnoreCase(Integer gradeFilter, String searchValue, Pageable pageable);
    List<SchoolClass> findAllByAssessmentsContaining(Assessment assessment);
    List<SchoolClass> findAllByGrade(Integer grade);
    @Query("SELECT COUNT(st) FROM Student st WHERE st.schoolClass.id =:classId")
    Integer getClassStudentCount(@Param("clasId") Long classId);
}
