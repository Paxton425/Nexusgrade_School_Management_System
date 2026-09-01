package com.nexusgrade.app.controller;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.model.Department;
import com.nexusgrade.app.model.Instructor;
import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.InstructorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/instructors")
public class InstructorController {
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    InstructorRepository instructorRepository;
    InstructorController(InstructorRepository instructorRepository){
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public  String getAllInstructors(Model model){
        List<Instructor> instructors = instructorRepository.findAll();
        model.addAttribute("instructors", instructors);
        return "instructors/instructors-list";
    }

    @GetMapping("/view/{id}")
    public  String getInstructor(@PathVariable UUID id, Model model){
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Instructor not found"));
        model.addAttribute("instructor", instructor);
        return "instructors/instructor-view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("departments", Department.values());
        model.addAttribute("titles", Instructor.Title.values());
        model.addAttribute("genders", User.Gender.values());
        return "instructors/instructor-form";
    }
    @GetMapping("/edit/{id}")
    public String showCreateForm(@PathVariable UUID id, Model model) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Instructor not found"));

        model.addAttribute("instructor", instructor);
        model.addAttribute("departments", Department.values());
        model.addAttribute("titles", Instructor.Title.values());
        model.addAttribute("genders", User.Gender.values());
        return "instructors/instructor-form";
    }

    @LogActivity(action = "updated an instructor", entityType = "INSTRUCTOR")
    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute Instructor instructor, RedirectAttributes ra) {
        /*
        try {
            boolean isEdit = (instructor.getId() != null);
            instructorRepository.save(instructor);
            String msg = isEdit ? "updated" : "Registered";
            ra.addFlashAttribute("success", "Instructor " + instructor.getFirstNames() + " " + msg + " successfully!");
            return "redirect:/instructors";
        } catch (Exception e) {
            logger.error("Save failed", e);
            ra.addFlashAttribute("error", "Save failed: " + e.getMessage());
            return instructor.getId() == null ? "redirect:/instructors/create" : "redirect:/instructors/edit/" + instructor.getId();
        }
        */
        return "/instructors/instructors-list";
    }

}
