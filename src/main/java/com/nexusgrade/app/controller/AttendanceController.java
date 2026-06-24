package com.nexusgrade.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/classes/attendance")
public class AttendanceController {

    @GetMapping("/{classId}")
    public String viewClassAttendance(@PathVariable String classId, Model model) {

        // Demo Data - Class Info
        model.addAttribute("className", "Algebra 2 - Honors");
        model.addAttribute("classPeriod", "3rd Period");
        model.addAttribute("teacherName", "Mrs. Sarah Johnson");
        model.addAttribute("roomNumber", "Room 204");
        model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        model.addAttribute("totalStudents", 28);
        model.addAttribute("presentCount", 24);
        model.addAttribute("absentCount", 3);
        model.addAttribute("lateCount", 1);

        // Demo Data - Student Roster
        List<StudentAttendance> students = new ArrayList<>();

        students.add(new StudentAttendance("S001", "Emma Thompson", "11th", "emma.t@email.com", "Present", false));
        students.add(new StudentAttendance("S002", "James Rodriguez", "11th", "james.r@email.com", "Present", false));
        students.add(new StudentAttendance("S003", "Olivia Chen", "11th", "olivia.c@email.com", "Absent", true));
        students.add(new StudentAttendance("S004", "William Park", "11th", "william.p@email.com", "Present", false));
        students.add(new StudentAttendance("S005", "Sophia Martinez", "11th", "sophia.m@email.com", "Late", false));
        students.add(new StudentAttendance("S006", "Liam O'Brien", "11th", "liam.o@email.com", "Present", false));
        students.add(new StudentAttendance("S007", "Ava Nakamura", "11th", "ava.n@email.com", "Present", false));
        students.add(new StudentAttendance("S008", "Noah Williams", "11th", "noah.w@email.com", "Absent", false));
        students.add(new StudentAttendance("S009", "Mia Johnson", "11th", "mia.j@email.com", "Present", false));
        students.add(new StudentAttendance("S010", "Ethan Brown", "11th", "ethan.b@email.com", "Present", false));
        students.add(new StudentAttendance("S011", "Isabella Garcia", "11th", "isabella.g@email.com", "Present", false));
        students.add(new StudentAttendance("S012", "Alexander Kim", "11th", "alex.k@email.com", "Absent", true));
        students.add(new StudentAttendance("S013", "Charlotte Davis", "11th", "charlotte.d@email.com", "Present", false));
        students.add(new StudentAttendance("S014", "Michael Thompson", "11th", "michael.t@email.com", "Present", false));
        students.add(new StudentAttendance("S015", "Amelia White", "11th", "amelia.w@email.com", "Present", false));

        model.addAttribute("students", students);

        // Statistics for the summary cards
        model.addAttribute("attendanceRate", "85.7%");
        model.addAttribute("excusedAbsences", 2);
        model.addAttribute("unexcusedAbsences", 1);

        return "attendance/attendance-view";
    }

    // Inner class for demo data
    public static class StudentAttendance {
        private String id;
        private String name;
        private String grade;
        private String email;
        private String status; // Present, Absent, Late
        private boolean hasExcuse;

        public StudentAttendance(String id, String name, String grade, String email, String status, boolean hasExcuse) {
            this.id = id;
            this.name = name;
            this.grade = grade;
            this.email = email;
            this.status = status;
            this.hasExcuse = hasExcuse;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public boolean isHasExcuse() { return hasExcuse; }
        public void setHasExcuse(boolean hasExcuse) { this.hasExcuse = hasExcuse; }
    }
}
