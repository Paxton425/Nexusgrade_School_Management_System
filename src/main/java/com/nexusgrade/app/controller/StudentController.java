package com.nexusgrade.app.controller;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.dto.*;
import com.nexusgrade.app.dto.StudentDTO;
import com.nexusgrade.app.dto.StudentGradeSummaryDTO;
import com.nexusgrade.app.dto.StudentReportDTO;
import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.StudentReport;
import com.nexusgrade.app.model.Subject;
import com.nexusgrade.app.repository.ClassRepository;
import com.nexusgrade.app.repository.ReportRepository;
import com.nexusgrade.app.repository.StudentRepository;
import com.nexusgrade.app.repository.SubjectRepository;
import com.nexusgrade.app.service.DashboardService;
import com.nexusgrade.app.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController implements CommandLineRunner {
    @Autowired
    ReportService reportService;
    @Autowired
    DashboardService dashboardService;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private ClassRepository classRepository;
    private ReportRepository reportRepository;

    StudentController(StudentRepository studentRepository,
                      SubjectRepository subjectRepository,
                      ClassRepository classRepository,
                      ReportRepository reportRepository){
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students/students-list";
    }

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentsJson(
            @RequestParam(defaultValue = "1") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue) {

        Pageable pageable = PageRequest.of(start / length, length, Sort.by("lastName").ascending());

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);

        if (searchValue != null && !searchValue.isEmpty()) {
            // ⚡ SLICE: One trip to the DB (380ms)
            Slice<Student> slice = studentRepository.searchStudents(searchValue, pageable);

            response.put("data", slice.getContent().stream().map(StudentDTO::new).toList());

            // DataTables needs a number for 'recordsFiltered'.
            // With Slice, we don't know the total. We can use a high number or
            // just the current count + 1 if there's a next page to keep the "Next" button active.
            response.put("recordsFiltered", slice.hasNext() ? start + length + 1 : start + slice.getNumberOfElements());
        } else {
            // For the main list, we can use Page or a cached count
            Page<Student> page = studentRepository.findAll(pageable);
            response.put("data", page.getContent().stream().map(StudentDTO::new).toList());
            response.put("recordsFiltered", page.getTotalElements());
        }

        // Use a cached count for recordsTotal so we don't hit the DB across the ocean every time
        response.put("recordsTotal", studentRepository.count());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{uuid}")
    public String findStudentById(@PathVariable UUID uuid, Model model){
        Student student = studentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Subject ID " + uuid + " not found"));

        model.addAttribute(student);
        return "students/student-profile";
    }

    @GetMapping(path = "/search")
    @ResponseBody
    public ResponseEntity<?> getBySearchFilter(@RequestParam String search){
        try {
            String searchLower = search.toLowerCase();
            List<StudentDTO> matches = studentRepository.findAll().stream()
                    .filter(s -> {
                        String first = s.getFirstName() != null ? s.getFirstName().toLowerCase() : "";
                        String last = s.getLastName() != null ? s.getLastName().toLowerCase() : "";
                        return first.contains(searchLower) || last.contains(searchLower);
                    })
                    .map(s -> new StudentDTO(s))
                    .toList();

            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            logger.error("Search error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong " + e.getMessage());
        }
    }

    @GetMapping("/grades")
    public String getGradesSummary(Model model){
        List<StudentGradeSummaryDTO> studentsSummaries = studentRepository.findAll().stream()
                .map(s -> new StudentGradeSummaryDTO(s))
                .toList();
        model.addAttribute("studentsSummaries", studentsSummaries);
        return "students/grades-summaries";
    }

    @GetMapping("/create")
    public String CreateStudentForm(Model model) {
        List<Subject> subjects = subjectRepository.findAll();
        Map<Integer, List<SchoolClass>> classes = classRepository.findAll().stream()
                .collect(Collectors.groupingBy(SchoolClass::getGrade));
        model.addAttribute("student", new Student());
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("classes", classes);
        model.addAttribute("statuses", Student.Status.values());
        return "students/student-form";
    }

    @GetMapping("/edit/{uuid}")
    public String editStudentForm(@PathVariable UUID uuid, Model model) {
        Student student = studentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Map<Integer, List<SchoolClass>> classes = classRepository.findAll().stream()
                .collect(Collectors.groupingBy(SchoolClass::getGrade));

        model.addAttribute("student", student); // This student has an ID and data
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("classes", classes);
        model.addAttribute("statuses", Student.Status.values());

        return "students/student-form"; // Use the SAME file
    }

    @LogActivity(action = "updated a student", entityType = "STUDENT")
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student, RedirectAttributes ra) {
        try {
            boolean isEdit = (student.getId() != null);
            studentRepository.save(student);

            String msg = isEdit ? "updated" : "enrolled";
            ra.addFlashAttribute("success", "Student " + student.getFirstName() + " " + msg + " successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            logger.error("Save failed", e);
            ra.addFlashAttribute("error", "Save failed: " + e.getMessage());
            return student.getId() == null ? "redirect:/students/create" : "redirect:/students/edit/" + student.getId();
        }
    }

    @LogActivity(action = "deleted a student", entityType = "STUDENT")
    @GetMapping("/delete/{id}")
    public String saveStudent(@PathVariable UUID id, RedirectAttributes ra) {
        try {
            studentRepository.deleteById(id);
            ra.addFlashAttribute("success", "Student deleted successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            logger.error("delete failed", e);
            ra.addFlashAttribute("error", "delete failed: " + e.getMessage());
            return id == null ? "redirect:/students" : "redirect:/students/student/" + id;
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }
}