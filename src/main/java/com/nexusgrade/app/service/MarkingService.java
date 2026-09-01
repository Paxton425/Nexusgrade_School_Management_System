package com.nexusgrade.app.service;

import com.nexusgrade.app.dto.AssessmentDTO;
import com.nexusgrade.app.dto.AssessmentScoreDTO;
import com.nexusgrade.app.dto.MarkDTO;
import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.AssessmentMark;
import com.nexusgrade.app.model.AssessmentScore;
import com.nexusgrade.app.repository.AssessmentMarkRepository;
import com.nexusgrade.app.repository.AssessmentScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkingService {

    @Autowired
    private AssessmentMarkRepository markRepository;
    @Autowired
    private AssessmentScoreRepository scoreRepository;

    Logger logger = LoggerFactory.getLogger(MarkingService.class);

    public List<AssessmentScore> markAllScores(List<AssessmentScore> scores){
        for(AssessmentScore score : scores)
            try{
                if(score.getScore() != null && score.getAssessment().getMaxPoints()!= null){
                    Double mark = computeMark(score.getScore(), score.getAssessment().getMaxPoints());
                    AssessmentMark assessmentMark = new AssessmentMark(null, mark, score, null, null);
                    score.setAssessmentMark(assessmentMark);
                } else {
                    throw new NullPointerException("Null Score or Assessment Max Points!!");
                }
            } catch(Exception e){
                logger.error("Error Marking Score, with ID: {}", score.getId());
                e.printStackTrace();
            }
        return scoreRepository.saveAll(scores);
    }

    public AssessmentScore markScore(AssessmentScore score){
        try{
            if(score.getScore() != null && score.getAssessment().getMaxPoints()!= null){
                Double mark = computeMark(score.getScore(), score.getAssessment().getMaxPoints());
                AssessmentMark assessmentMark = new AssessmentMark(null, mark, score, null, null);
                score.setAssessmentMark(assessmentMark);
                return scoreRepository.save(score);
            } else {
                throw new NullPointerException("Null Score or Assessment Max Points!!");
            }
        } catch (Exception e) {
            logger.error("Error Marking Score, with ID: {}", score.getId());
            e.printStackTrace();
            return null;
        }
    }

    public double computeMark(Integer score, Integer maxPoints){
        if (maxPoints == null || maxPoints == 0) return 0.0;

        double percentage = (score / (double) maxPoints) * 100;
        // Round to 2 decimal places using HALF_UP (standard rounding rules)
        return BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
