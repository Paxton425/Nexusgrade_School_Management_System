package com.nexusgrade.app.controller;

import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.StudentReport;
import com.nexusgrade.app.repository.ReportRepository;
import com.nexusgrade.app.repository.StudentRepository;
import com.nexusgrade.app.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/result")
public class ReportController {

    @Autowired
    ReportService reportService;

    ReportRepository reportRepository;
    StudentRepository studentRepository;
    ReportController(ReportRepository reportRepository, StudentRepository studentRepository){
        this.reportRepository = reportRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/student-report/{studentId}")
    public String getStudentReport(@PathVariable UUID studentId, Model model) {
        try{
            StudentReport report = reportRepository.findStudentReportByStudent_Id(studentId)
                    .orElseGet(()->{
                        Student student = studentRepository.findById(studentId)
                                .orElseThrow(()-> new EntityNotFoundException("Student matching id not found"));
                        return reportService.generateNewReportTemplate(student);
                    });
            Map<Result.Term, Double[]> termOveralls = reportService.getTermOveralls(report);
            model.addAttribute("report", report);
            model.addAttribute("termOveralls", termOveralls);
            model.addAttribute("termPeriods", Result.Term.values());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return "students/report";
    }
}
