package com.example.pairofemployeesspring.controller;

import com.example.pairofemployeesspring.EmployeePairResult;
import com.example.pairofemployeesspring.Util;
import com.example.pairofemployeesspring.service.EmployeeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController

public class EmployeeAnalysisController {
    @Autowired
    EmployeeAnalysisService employeeAnalysisService;

    @PostMapping("/upload")
    public ResponseEntity<List<EmployeePairResult>> analyzeUploadedFile(@RequestParam("file") MultipartFile file) {
        Map<String, Map<String, LocalDate[]>> employeeProjects = new HashMap<>();
        Util.csvReader(employeeProjects, file);
        List<EmployeePairResult> result = employeeAnalysisService.analyzeEmployeeProjects(employeeProjects);


        return ResponseEntity.ok(result);
    }
}

