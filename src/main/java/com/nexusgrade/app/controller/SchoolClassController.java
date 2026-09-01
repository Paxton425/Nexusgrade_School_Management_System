package com.nexusgrade.app.controller;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.*;
import com.nexusgrade.app.service.AcademicCalendarService;
import com.nexusgrade.app.service.AttendanceService;
import com.nexusgrade.app.service.TimeTableService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping(path = "/classes")
public class SchoolClassController {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    TimeTableService timeTableService;
    @Autowired
    AcademicCalendarService calendarService;

    private ClassRepository classRepository;
    private SubjectRepository subjectRepository;
    private TimeTableRepository timeTableRepository;
    private AssessmentScoreRepository assessmentScoreRepository;
    private AcademicCalendarRepository calendarRepository;
    private StatsRepository statsRepository;

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    SchoolClassController(AssessmentScoreRepository assessmentScoreRepository,
                          SubjectRepository subjectRepository,
                          ClassRepository classRepository,
                          AcademicCalendarRepository calendarRepository,
                          StatsRepository statsRepository,
                          TimeTableRepository timeTableRepository){
        this.classRepository = classRepository;
        this.assessmentScoreRepository = assessmentScoreRepository;
        this.subjectRepository = subjectRepository;
        this.calendarRepository = calendarRepository;
        this.statsRepository = statsRepository;
        this.timeTableRepository = timeTableRepository;
    }

    @GetMapping("")
    public String getAllClasses(Model model){
        List<SchoolClass> classes = classRepository.findAll();
        model.addAttribute("classes", classes);
        return "classes/classes-list";
    }

    @GetMapping("/view/{id}")
    public String getSchoolClassView(@PathVariable long id, Model model){
        SchoolClass schoolClass = classRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Class Not found"));
        model.addAttribute("schoolClass", schoolClass);

        // Attendance metrics
        model.addAttribute("todayAttendanceRate", attendanceService.getTodayClassAttendanceRate(id));
        model.addAttribute("weeklyAttendanceAvg", attendanceService.getWeeklyAverage(id));
        model.addAttribute("monthlyAttendanceAvg", attendanceService.getMonthlyAverage(id));
        model.addAttribute("absentToday", attendanceService.getAbsentCountToday(id));
        model.addAttribute("overallPerformance", attendanceService.getOverallPerformance(id));

        // Recent attendance records
        model.addAttribute("recentAttendance", attendanceService.getRecentAttendances(id, 10));

        return "classes/class-view";
    }

    @GetMapping("/classes/create")
    public String showCreateForm(Model model) {
        model.addAttribute("schoolClass", new SchoolClass());
        model.addAttribute("allSubjects", subjectRepository.findAll()); // Required for checkbox generation
        return "classes/class-form";
    }

    @LogActivity(action = "updated a class", entityType = "CLASS")
    @PostMapping("save")
    public String saveStudent(@ModelAttribute SchoolClass schoolClass, RedirectAttributes ra){
        try{
            boolean isEdit = (schoolClass.getId() != null);
            classRepository.save(schoolClass);

            String msg = isEdit ? "updated" : "enrolled";
            ra.addFlashAttribute("success", "Class " + schoolClass.getTitle() + " " + msg + " successfully!");
            return "redirect:/classes";
        } catch (Exception e){
            logger.error("Save failed", e);
            ra.addFlashAttribute("error", "Save failed: " + e.getMessage());
            return schoolClass.getId() == null ? "redirect:/classes/create" : "redirect:/classes/edit/" + schoolClass.getId();
        }
    }

    @GetMapping("/edit/{id}")
    public String getClassEditForm(@PathVariable long id, Model model){
        SchoolClass schoolClass = classRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Class Not found"));
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("allSubjects", subjects);

        return "classes/class-form";
    }

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getClassesJson(
            @RequestParam(defaultValue = "1") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(required = false) Integer gradeFilter) {

        Pageable pageable = PageRequest.of(start / length, length, Sort.by("grade").ascending());

        Page<SchoolClass> page;
        // Logic to handle both search text and grade filter
        if (gradeFilter != null) {
            page = classRepository.findByGradeAndTitleContainingIgnoreCase(gradeFilter, searchValue != null ? searchValue : "", pageable);
        } else if (searchValue != null && !searchValue.isEmpty()) {
            page = classRepository.findByTitleContainingIgnoreCase(searchValue, pageable);
        } else {
            page = classRepository.findAll(pageable);
        }

        // Map to a simple response list to avoid recursion/heavy loads
        List<Map<String, Object>> data = page.getContent().stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("title", c.getTitle());
            map.put("grade", c.getGrade());
            map.put("classYear", c.getClassYear());
            map.put("studentCount", c.getStudents().size());
            return map;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", classRepository.count());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("class/timetable/{classId}")
    public String viewClassTimetable(@PathVariable Long classId, Model model) {

        SchoolClass schoolClass = classRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Class not found for ID: " + classId));

        TimeTable timeTable = schoolClass.getClassTimeTable();
        UUID timeTableId = (timeTable != null) ? timeTable.getId() : null;

        // 1. Fetch time slots first
        List<String> timeSlots = timeTableService.getTimeSlots(timeTableId);
        List<String> days = timeTableService.getDays();

        // 2. Build aligned matrix map
        Map<String, List<TimeTableService.TimeSlot>> timetable = timeTableService.buildTimeTable(timeTable, timeSlots);

        // 3. Class Teacher Info (Null-safe)
        String classTeacherName = "Unassigned";
        if (schoolClass.getClassTeacher() != null && schoolClass.getClassTeacher().getUser() != null) {
            User user = schoolClass.getClassTeacher().getUser();
            String title = (user.getTitle() != null) ? user.getTitle().getLabel() : "";
            classTeacherName = (title + " " + user.getLastName()).trim();
        }

        // 4. Academic Calendar
        AcademicCalendar calendar = calendarService.getCurrentTermsCalender();
        String currentTerm = (calendar != null) ? calendar.getCurrentTerm().toString() : "Term 1";

        // 5. Populate Model
        model.addAttribute("className", "Grade " + schoolClass.getGrade() + "-" + schoolClass.getTitle());
        model.addAttribute("academicYear", schoolClass.getClassYear());
        model.addAttribute("semester", currentTerm);
        model.addAttribute("classTeacher", classTeacherName);
        model.addAttribute("totalStudents", classRepository.getClassStudentCount(classId));
        model.addAttribute("roomNumber", "Class Room " + schoolClass.getGrade() + "-" + schoolClass.getTitle());

        model.addAttribute("days", days);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("timetable", timetable);

        // Statistics
        Map<String, Integer> stats = timeTableService.getStats(timeTableId);
        model.addAttribute("totalPeriods", stats.get("totalPeriods"));
        model.addAttribute("subjectsCount", stats.get("subjectsCount"));

        return "classes/timetable";
    }
}
