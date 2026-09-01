package com.nexusgrade.app.controller;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.model.Department;
import com.nexusgrade.app.model.Subject;
import com.nexusgrade.app.repository.AssessmentScoreRepository;
import com.nexusgrade.app.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    Logger logger = LoggerFactory.getLogger(SubjectController.class);
    private SubjectRepository subjectRepository;
    private AssessmentScoreRepository assessmentScoreRepository;

    SubjectController(AssessmentScoreRepository assessmentScoreRepository,
                      SubjectRepository subjectRepository){
        this.assessmentScoreRepository = assessmentScoreRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("")
    public String listSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer grade, // New parameter
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("grade").ascending().and(Sort.by("name")));
        Page<Subject> subjectPage;

        if (grade != null) {
            subjectPage = subjectRepository.findSubjectsByGrade(grade, pageable);
            model.addAttribute("selectedGrade", grade);
        } else {
            subjectPage = subjectRepository.findAll(pageable);
        }

        model.addAttribute("subjectPage", subjectPage);
        return "subjects/subject-list";
    }

    @GetMapping("/view/{id}")
    public String viewSubject(@PathVariable Long id, Model model) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Subject not found"));
        model.addAttribute("subject", subject);
        return "subjects/subject-view";
    }

    @GetMapping("/subject/grades/{id}")
    public String viewSubjectEnrollmentGrades(@PathVariable Long id, Model model) {
        // Find the specific record that links this student to this subject
        return "subjects/subject-grades";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("departments", Department.values());
        return "subjects/subject-form";
    }

    @GetMapping("/edit/{id}")
    public String showCreateForm(@PathVariable long id, Model model) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Subject not found!"));
        model.addAttribute("subject", subject);
        model.addAttribute("departments", Department.values());
        return "subjects/subject-form";
    }

    @LogActivity(action = "updated a subject", entityType = "SUBJECT")
    @PostMapping("/save")
    public String saveSubject(@ModelAttribute Subject subject, RedirectAttributes ra) {
        try {
            boolean isEdit = (subject.getId() != null);
            String message = (isEdit)? "updated" : "created";
            subjectRepository.save(subject);
            ra.addFlashAttribute("success", "Subject "+message+" successfully!");
            return "redirect:/subjects/view/"+subject.getId();
        } catch (Exception e){
            logger.error("Save failed", e);
            ra.addFlashAttribute("error", "Subject save failed!");
            return (subject.getId() == null)? "redirect:/subjects/create" : "redirect:/subjects/edit/" + subject.getId();
        }
    }
}
