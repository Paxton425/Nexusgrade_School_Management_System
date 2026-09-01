package com.nexusgrade.app.service;

import com.nexusgrade.app.model.AcademicCalendar;
import com.nexusgrade.app.model.Stats;
import com.nexusgrade.app.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    StatsRepository statsRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    InstructorRepository instructorRepository;
    @Autowired
    AssessmentScoreRepository assessmentScoreRepository;
    @Autowired
    AcademicCalendarRepository calendarRepository;
    @Autowired
    AttendanceRepository attendanceRepository;

    Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    public Stats generateNewLatestStats(){
        try{
            Integer studentCount = (int) studentRepository.count();
            Integer instructorCount = (int) instructorRepository.count();
            Double passRates = calculatePassRates();
            Double overAllAverage = calculateOverallAverage();
            Double attendanceRate = calculateAttendanceRate();
            AcademicCalendar ac = calendarRepository.findCurrentTermsCalender()
                    .orElseThrow(()-> new EntityNotFoundException("Current Term calendar not found!!"));

            Stats stats = new Stats(
                    null,
                    studentCount,
                    instructorCount,
                    attendanceRate,
                    overAllAverage,
                    passRates,
                    ac);

            return statsRepository.save(stats);
        } catch (Exception e) {
            logger.error("Stats generation failure!");
            e.printStackTrace();
            return null;
        }
    }

    private Double calculatePassRates(){
        return null;
    }
    private Double calculateOverallAverage(){
        return assessmentScoreRepository.getOverallAverageMark();
    }
    private Double calculateAttendanceRate(){
        return null;
    }
}
