package com.nexusgrade.app.controller;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.dto.SchoolClassDTO;
import com.nexusgrade.app.dto.SubmissionStats;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.*;
import com.nexusgrade.app.service.AssessmentService;
import com.nexusgrade.app.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/assessments", method = RequestMethod.GET)
public class AssessmentsController {
    @Autowired
    AssessmentService assessmentService;
    @Autowired
    ReportService reportService;

    private AssessmentRepository assessmentRepository;
    private SubjectRepository subjectRepository;
    private AssessmentScoreRepository assessmentScoreRepository;
    private StudentRepository studentRepository;
    private ClassRepository classRepository;
    private AcademicCalendarRepository calendarRepository;
    AssessmentsController(AssessmentRepository assessmentRepository,
                          SubjectRepository subjectRepository,
                          AssessmentScoreRepository assessmentScoreRepository,
                          StudentRepository studentRepository,
                          ClassRepository classRepository,
                          AcademicCalendarRepository calendarRepository){
        this.assessmentRepository = assessmentRepository;
        this.subjectRepository = subjectRepository;
        this.assessmentScoreRepository = assessmentScoreRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.calendarRepository = calendarRepository;
    }

    @GetMapping("")
    public String getAssessments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String subject,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        Page<Assessment> assessmentPage;
        if(!search.isEmpty() || !subject.isEmpty() || !type.isEmpty()){ // Fetch Filtered Data
            Assessment.AssessmentType AType = (!type.isEmpty())?
                Assessment.AssessmentType.valueOf(type.toUpperCase()) :
                null;
            assessmentPage = assessmentRepository.findWithFilters(search, subject, AType, pageable);
        }
        else { //Unfiltered data
            assessmentPage = assessmentRepository.findAll(pageable);
        }

        model.addAttribute("assessments", assessmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", assessmentPage.getTotalPages());
        model.addAttribute("totalItems", assessmentPage.getTotalElements());

        // Keep filters in model to persist values in the UI
        model.addAttribute("selectedSubject", subject);
        model.addAttribute("selectedType", type);
        model.addAttribute("searchQuery", search);

        return "assessments/assessment-list";
    }

    @GetMapping("/assessment/{id}")
    public String getaAssessment(@PathVariable(value = "id") Long id, Model model){
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment with ID " + id + " not found"));

        AssessmentService.TimeProgess timeProgress = assessmentService
                .calculateTimeProgress(assessment.getAssignmentIssueDate(),
                        assessment.getAssignmentDeadline());

        List<AssessmentScore> submissions = assessmentScoreRepository.findByAssessment(assessment);
        List<SchoolClass> assignedClasses = assessment.getSchoolClasses();
        long totalEnrolled = 0;
        for(SchoolClass sClass: assignedClasses)
            totalEnrolled += sClass.getStudents().size();

        SubmissionStats stats = new SubmissionStats(
                totalEnrolled,
                submissions.size(),
                submissions.stream().mapToInt(AssessmentScore::getScore).average().orElse(0.0),
                assessment.getMaxPoints(),
                submissions.stream().filter(r -> r.getScore() >= (assessment.getMaxPoints() * 0.5)).count()
        );

        model.addAttribute("assessment", assessment);
        model.addAttribute("submissions", submissions);
        model.addAttribute("stats", stats);
        model.addAttribute("timeProgress", timeProgress.progress);
        model.addAttribute("statusText", timeProgress.statusText);
        model.addAttribute("isOverdue", timeProgress.daysLeft < 0);
        model.addAttribute("assessment", assessment);

        return "assessments/assessment";
    }

    @GetMapping("/submissions/{assessmentId}")
    public String viewSubmissions(@PathVariable Long assessmentId, Model model) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        List<AssessmentScore> submissions = assessmentScoreRepository.findByAssessment(assessment);
        List<SchoolClass> assignedClasses = assessment.getSchoolClasses();

        long totalEnrolled = 0;
        for(SchoolClass sClass : assignedClasses) {
            totalEnrolled += sClass.getStudents().size();
        }

        // Ensure we don't divide by zero if no students are enrolled
        double averageScore = submissions.stream()
                .mapToInt(AssessmentScore::getScore)
                .average()
                .orElse(0.0);

        long passCount = submissions.stream()
                .filter(r -> r.getScore() >= (assessment.getMaxPoints() * 0.5))
                .count();

        // Create the stats object
        SubmissionStats stats = new SubmissionStats(
                totalEnrolled,
                submissions.size(),
                averageScore,
                assessment.getMaxPoints(),
                passCount
        );

        model.addAttribute("assessment", assessment);
        model.addAttribute("submissions", submissions);
        model.addAttribute("stats", stats);

        return "assessments/submissions";
    }

    @GetMapping("/create")
    public String createAssessment(Model model){
        List<Subject> subjects = subjectRepository.findAll();
        List<SchoolClass> schoolClasses = classRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        String schoolClassesJSON = mapper.writeValueAsString(SchoolClassDTO.ListOf(schoolClasses));

        model.addAttribute("assessment", new Assessment());
        model.addAttribute("assessmentTypes", Assessment.AssessmentType.values());
        model.addAttribute("schoolClasses", schoolClasses);
        model.addAttribute("schoolClassesJSON", schoolClassesJSON);
        model.addAttribute("subjects", subjects);
        return "assessments/assessment-form";
    }

    @GetMapping("/edit/{id}")
    public String createAssessment(@PathVariable Long id, Model model){
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment with ID " + id + " not found"));
        List<Subject> subjects = subjectRepository.findAll();
        List<SchoolClass> schoolClasses = classRepository.findAll();
        List<SchoolClass> existingSchoolClass = classRepository.findAllByAssessmentsContaining(assessment);

        ObjectMapper mapper = new ObjectMapper();
        String schoolClassesJSON = mapper.writeValueAsString(SchoolClassDTO.ListOf(schoolClasses));
        String existingSchoolClassesJSON = mapper.writeValueAsString(SchoolClassDTO.ListOf(existingSchoolClass));

        model.addAttribute("assessment", assessment);
        model.addAttribute("assessmentTypes", Assessment.AssessmentType.values());
        model.addAttribute("schoolClasses", schoolClasses);
        model.addAttribute("schoolClassesJSON", schoolClassesJSON);
        model.addAttribute("existingSchoolClassesJSON", existingSchoolClassesJSON);
        model.addAttribute("subjects", subjects);
        return "assessments/assessment-form";
    }

    @LogActivity(action = "updated an assessment template", entityType = "ASSESSMENT")
    @PostMapping("/save")
    public String saveAssessment(@ModelAttribute("assessment") Assessment assessment,
                                 RedirectAttributes redirectAttributes) {
        try {
            assessmentRepository.save(assessment);
            redirectAttributes.addFlashAttribute("message", "Assessment '" + assessment.getTitle() + "' saved successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error saving assessment: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/assessment-list";
    }

    @LogActivity(action = "deleted an assessment template", entityType = "ASSESSMENT")
    @GetMapping("/delete/{id}")
    public String deleteAssessment(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            assessmentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Assessment deleted successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting assessment: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/assessments/assessment/{id}";
        }
        return "redirect:/assessment-list";
    }

    @GetMapping("/record/{assessmentId}")
    public String showRecordMarkForm(@PathVariable Long assessmentId, Model model) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        // We only want students doing the assessment
        List<SchoolClass> assignedClasses = assessment.getSchoolClasses();
        List<Student> enrolledStudents = new ArrayList<>();
        for(SchoolClass sClass: assignedClasses)
            for(Student student: sClass.getStudents())
                enrolledStudents.add(student);

        AssessmentScore score = new AssessmentScore();
        List<AcademicCalendar> currentYearCalendars = calendarRepository.findAllByAcademicYear(LocalDate.now().getYear());
        score.setAssessment(assessment); // Pre-link the assessment

        model.addAttribute("assessment", assessment);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("score", score);
        model.addAttribute("calenders", currentYearCalendars);

        return "assessments/submission-form";
    }

    @LogActivity(action = "updated assessment marks", entityType = "RESULT")
    @PostMapping("/scores/save")
    public String saveAssessmentScore(@ModelAttribute AssessmentScore score, RedirectAttributes ra) {
        // Basic validation to ensure they didn't Exceed the HTML max attribute
        if (score.getScore() > score.getAssessment().getMaxPoints()) {
            ra.addFlashAttribute("message", "Score cannot exceed maximum points!");
            ra.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/assessments/record/" + score.getAssessment().getId();
        }

        AssessmentScore savedAssessmentScores = assessmentScoreRepository.save(score);

        reportService.refreshStudentReport(savedAssessmentScores.getStudent()); //Refresh report after marks updates

        ra.addFlashAttribute("message", "Mark recorded for student successfully!");
        ra.addFlashAttribute("alertClass", "alert-success");

        // Redirect back to the assessment list or a "view scores" page
        return "redirect:/assessments/assessment/"+score.getAssessment().getId();
    }
}
