package com.nexusgrade.app.service;

import com.nexusgrade.app.model.AcademicCalendar;
import com.nexusgrade.app.model.Term;
import com.nexusgrade.app.repository.AcademicCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicCalendarService {

    @Autowired
    AcademicCalendarRepository calendarRepository;

    public Term getCurrentTerm(){
        AcademicCalendar calendar = calendarRepository.findCurrentTermsCalender()
                .orElseThrow(()-> new NullPointerException("Current Term Not Found!"));

        return calendar.getCurrentTerm();
    }

    public AcademicCalendar getCurrentTermsCalender(){
        return calendarRepository.findCurrentTermsCalender()
                .orElseThrow(()-> new NullPointerException("Current Term Not Found!"));
    }

    public List<AcademicCalendar> getCurrentYearTerms(Integer year){
        return calendarRepository.findCalendarsByAcademicYear(year);
    }
}
