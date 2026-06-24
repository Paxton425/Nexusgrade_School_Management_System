package com.nexusgrade.app.service;

import com.nexusgrade.app.model.ClassAttendance;
import com.nexusgrade.app.model.ClassSession;
import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.repository.AttendanceRepository;
import com.nexusgrade.app.repository.ClassAttendanceRepository;
import com.nexusgrade.app.repository.ClassSessionRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    ClassAttendanceRepository classAttendanceRepository;
    @Autowired
    ClassSessionRepository classSessionRepository;

    // Teacher marks students PRESENT/ABSENT/LATE
    List<ClassAttendance> markAttendances(List<ClassAttendance> classAttendances){
        return classAttendanceRepository.saveAll(classAttendances);
    }
    // Show all attendances for one class period
    List<ClassAttendance> getSessionAttendance(ClassSession session){
        List<ClassAttendance> sessionAttendances = classAttendanceRepository.findClassAttendancesByClassSession(session);
        return sessionAttendances != null? sessionAttendances : Collections.emptyList();
    }

    // Show a student's attendance history - Null Safe
    public List<ClassAttendance> getStudentAttendance(Student student) {
        if (student == null || student.getId() == null) {
            return Collections.emptyList();
        }

        List<ClassAttendance> studentAttendances = classAttendanceRepository.findClassAttendancesByStudent(student);
        return studentAttendances != null ? studentAttendances : Collections.emptyList();
    }

    public List<ClassAttendance> getRecentAttendancesForClass(Long classId, int limit){
        ClassSession lastSession = classSessionRepository.findLastSessionByClassId(classId)
                .orElse(null);

        return getAttendancesBySession(lastSession, limit);
    }

    public List<ClassAttendance> getRecentAttendances(long classId, int limit) {
        // Find last class session
        ClassSession session = classSessionRepository
                .findLastSessionByClassId(classId)
                .orElse(null);
        return getAttendancesBySession(session, limit);
    }

    public List<ClassAttendance> getAttendancesBySession(ClassSession session, int limit){
        // Default limit
        int defaultLimit = limit > 0 ? limit : 10;

        // If no session exists, return empty list
        if (session == null) {
            return Collections.emptyList();
        }

        // Get attendance for that session
        List<ClassAttendance> attendances = session.getClassAttendances();

        // Return limited results (or all if less than limit)
        return attendances != null && !attendances.isEmpty()
                ? attendances.stream().limit(defaultLimit).collect(Collectors.toList())
                : Collections.emptyList();
    }

    // System marks missing records as ABSENT at 2 AM
    List<ClassAttendance> autoMarkAbsentStudents(){
        return null;
    }

    public List<ClassAttendance> getTodayClassAttendance(Long classId){
        LocalDate todayDate = LocalDate.now();
        return classAttendanceRepository.getClassAttendanceByDate(classId, todayDate);
    }
    public Double getTodayClassAttendanceRate(Long classId) {
        LocalDate todayDate = LocalDate.now();
        return classAttendanceRepository.getAttendancePercentByClassIdAndDate(classId, todayDate);
    }

    public @Nullable Object getWeeklyAverage(long id) {
        return null;
    }

    public @Nullable Object getMonthlyAverage(long id) {
        return null;
    }

    public @Nullable Object getAbsentCountToday(long id) {
        return null;
    }

    public @Nullable Object getOverallPerformance(long id) {
        return null;
    }
}
