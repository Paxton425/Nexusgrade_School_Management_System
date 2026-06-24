package com.nexusgrade.app.controller;

import com.nexusgrade.app.dto.ResultDTO;
import com.nexusgrade.app.model.ActivityLog;
import com.nexusgrade.app.model.Result;
import com.nexusgrade.app.repository.StudentRepository;
import com.nexusgrade.app.repository.InstructorRepository;
import com.nexusgrade.app.service.DashboardService;
import com.nexusgrade.app.service.GradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping(path = "/dashboard")
public class DashboardController {

    @Autowired GradingService gradingService;
    @Autowired DashboardService dashboardService;

    Logger logger = LoggerFactory.getLogger(DashboardController.class);

    StudentRepository studentRepository;
    InstructorRepository instructorRepository;
    DashboardController(StudentRepository studentRepository, InstructorRepository instructorRepository){
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public String getDashboardView(){
        return "dashboard/dashboard";
    }

    @GetMapping("/data")
    public ResponseEntity<?> dashboard() {

        logger.info("\n======= Handling Dashboard Request =======");
        try{
            Map<String, Object> dashboardData = dashboardService.getDashboardDataFromCache();
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            logger.error("Something went wrong while fetching dashboard data \n{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went wrong");
        }
    }

    @GetMapping("/gradeperfomance")
    ResponseEntity<?> getGradePerfomance(@RequestParam int grade){
        try{
            Map data = dashboardService.getSubjectPerformances(grade);
            return ResponseEntity.ok(data);
        } catch(Exception e){
            logger.error("Something went wron while getting grade performance \n{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not get grade perfomance!");
        }
    }

}
