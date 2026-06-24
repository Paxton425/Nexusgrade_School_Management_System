package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT(:searchValue, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT(:searchValue, '%'))")
    Slice<Student> searchStudents(@Param("searchValue") String searchValue, Pageable pageable);

    long countBySchoolClass(SchoolClass schoolClass);

    // Keep this for the initial "Total" count if DataTables absolutely needs it
    long count();
}
