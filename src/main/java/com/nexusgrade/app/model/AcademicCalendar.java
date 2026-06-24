package com.nexusgrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "academic_calendar")
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "academic_year", nullable = false)
    private int academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "term", nullable = false, length = 10)
    private Term term;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    @Column(name = "term_start_date")
    private LocalDate termStartDate;

    @Column(name = "term_end_date")
    private LocalDate termEndDate;

    @OneToMany(mappedBy = "academicCalendar")
    @JsonIgnore
    private List<Result> result;

    @OneToMany(mappedBy = "academicCalendar")
    @JsonIgnore
    private List<Stats> stats;

    // Constructors
    public AcademicCalendar() {}

    public AcademicCalendar(int academicYear, Term currentTerm, boolean isActive) {
        this.academicYear = academicYear;
        this.term = currentTerm;
        this.isActive = isActive;
    }

    public Long getId1() {
        return id;
    }

    public void setId1(Long id1) {
        this.id = id1;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getAcademicYear() { return academicYear; }
    public void setAcademicYear(int academicYear) { this.academicYear = academicYear; }

    public Term getCurrentTerm() { return term; }
    public void setCurrentTerm(Term currentTerm) { this.term = currentTerm; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDate getTermStartDate() { return termStartDate; }
    public void setTermStartDate(LocalDate termStartDate) { this.termStartDate = termStartDate; }

    public LocalDate getTermEndDate() { return termEndDate; }
    public void setTermEndDate(LocalDate termEndDate) { this.termEndDate = termEndDate; }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }
}