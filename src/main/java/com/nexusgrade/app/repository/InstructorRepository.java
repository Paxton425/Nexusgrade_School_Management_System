package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Instructor;
import com.nexusgrade.app.model.TimeTablePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    @Query("SELECT tp.instructor FROM TimeTablePeriod tp where tp.id =:periodId")
    Optional<Instructor> findByTimeTablePeriod(@Param("periodId") UUID periodId);
}
