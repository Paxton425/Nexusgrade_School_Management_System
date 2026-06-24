package com.nexusgrade.app.controller;

import com.nexusgrade.app.annotation.LogActivity;
import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Subject;
import com.nexusgrade.app.repository.ClassRepository;
import com.nexusgrade.app.repository.ResultRepository;
import com.nexusgrade.app.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/classes")
public class SchoolClassController {

    private ClassRepository classRepository;
    private SubjectRepository subjectRepository;
    private ResultRepository resultRepository;

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    SchoolClassController(ResultRepository resultRepository,
                          SubjectRepository subjectRepository,
                          ClassRepository classRepository){
        this.classRepository = classRepository;
        this.resultRepository = resultRepository;
        this.subjectRepository = subjectRepository;
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
}
