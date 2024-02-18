package com.example.pairofemployeesspring.service;

import com.example.pairofemployeesspring.EmployeePairResult;
import com.example.pairofemployeesspring.Util;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeAnalysisService {

    public List<EmployeePairResult> analyzeEmployeeProjects(Map<String, Map<String, LocalDate[]>> employeeProjects) {
        return pairWithLongestDurationOfCommonProjects(employeeProjects);
    }

    private static List<EmployeePairResult> pairWithLongestDurationOfCommonProjects(Map<String, Map<String, LocalDate[]>> employeeProjects) {
        String empId1 = "";
        String empId2 = "";
        long maxDuration = 0;

        List<String> employeeList = new ArrayList<>(employeeProjects.keySet());
        int n = employeeList.size();

        for (int i = 0; i < n - 1; i++) {
            String e1 = employeeList.get(i);
            for (int j = i + 1; j < n; j++) {
                String e2 = employeeList.get(j);
                long duration = Util.getCommonProjectDuration(employeeProjects.get(e1), employeeProjects.get(e2));
                if (duration > maxDuration) {
                    empId1 = e1;
                    empId2 = e2;
                    maxDuration = duration;
                }
            }
        }

        List<EmployeePairResult> resultList = new ArrayList<>();
        resultList.add(new EmployeePairResult(empId1, empId2, null, maxDuration));
        resultList.addAll(Util.outputOfEachProject(employeeProjects, empId1, empId2));

        return resultList;
    }
}
