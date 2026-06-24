package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findSubjectBySubjectCode(String code);
    @Query(value = "SELECT * FROM subject s WHERE s.grade = :grade ORDER BY s.subject_code LIMIT 7",
            nativeQuery = true)
    List<Subject> getSubjectsByGrade(@Param("grade") int grade);

    Page<Subject> findSubjectsByGrade(Integer grade, Pageable pageable);
}
