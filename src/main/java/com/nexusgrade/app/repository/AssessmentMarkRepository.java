package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.AssessmentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentMarkRepository extends JpaRepository<AssessmentMark, Long> {

    @Query(value = "SELECT m FROM AssessmentMark m ORDER BY m.mark DESC LIMIT 5")
    List<AssessmentMark> findTop5ByOrderByScoreDesc();
}
