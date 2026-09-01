package com.nexusgrade.app.dto;

import com.nexusgrade.app.model.AcademicCalendar;
import com.nexusgrade.app.model.Term;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

public class AcademicCalendarDTO {

    private Long id;
    private int academicYear; // e.g., 2026
    private Term term; // TERM_1, TERM_2, TERM_3, TERM_4
    private boolean isActive = false; // Flag to mark the current active system tracking block
    private LocalDate termStartDate;
    private LocalDate termEndDate;

    public AcademicCalendarDTO(AcademicCalendar academicCalendar) {
        this.id = academicCalendar.getId();
        this.academicYear = academicCalendar.getAcademicYear();
        this.term = academicCalendar.getCurrentTerm();
        this.isActive = academicCalendar.isActive();
        this.termStartDate = academicCalendar.getTermStartDate();
        this.termEndDate = academicCalendar.getTermEndDate();
    }

    public AcademicCalendarDTO(Long id, int academicYear, Term term, boolean isActive, LocalDate termStartDate, LocalDate termEndDate) {
        this.id = id;
        this.academicYear = academicYear;
        this.term = term;
        this.isActive = isActive;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(LocalDate termStartDate) {
        this.termStartDate = termStartDate;
    }

    public LocalDate getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(LocalDate termEndDate) {
        this.termEndDate = termEndDate;
    }
}
