package com.example.pairofemployeesspring;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d-M-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("M-d-yyyy"))
                .toFormatter();

        return LocalDate.parse(dateString, formatter);
    }

    public static void csvReader(Map<String, Map<String, LocalDate[]>> employeeProjects, MultipartFile file) {
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                String empId = row[0];
                String projectId = row[1];

                LocalDate dateFrom = parseDate(row[2]);
                LocalDate dateTo = row[3].equals("NULL") ? LocalDate.now() : parseDate(row[3]);

                Map<String, LocalDate[]> projectDates = employeeProjects.getOrDefault(empId, new HashMap<>());
                projectDates.put(projectId, new LocalDate[]{dateFrom, dateTo});
                employeeProjects.put(empId, projectDates);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long getDurationOverlap(LocalDate[] range1, LocalDate[] range2) {
        LocalDate start = range1[0].isBefore(range2[0]) ? range2[0] : range1[0];
        LocalDate end = range1[1].isBefore(range2[1]) ? range1[1] : range2[1];
        if (ChronoUnit.DAYS.between(start, end) < 0) {
            return 0;
        }
        else {
            return ChronoUnit.DAYS.between(start, end);
        }
    }


    public static List<EmployeePairResult> outputOfEachProject(Map<String, Map<String, LocalDate[]>> employeeProjects, String empId1, String empId2) {
        List<EmployeePairResult> result = new ArrayList<>();

        Map<String, LocalDate[]> projects1 = employeeProjects.get(empId1);
        Map<String, LocalDate[]> projects2 = employeeProjects.get(empId2);

        if (projects1 != null && projects2 != null) {
            for (Map.Entry<String, LocalDate[]> entry : projects1.entrySet()) {
                String projectId = entry.getKey();
                LocalDate[] dateRange1 = entry.getValue();

                if (projects2.containsKey(projectId)) {
                    LocalDate[] dateRange2 = projects2.get(projectId);
                    long duration = getDurationOverlap(dateRange1, dateRange2);

                    if (duration > 0) {
                        result.add(new EmployeePairResult(empId1, empId2, projectId, duration));
                    }
                }
            }
        }

        return result;
    }

    public static long getCommonProjectDuration(Map<String, LocalDate[]> projects1, Map<String, LocalDate[]> projects2) {
        long duration = 0;

        for (Map.Entry<String, LocalDate[]> entry : projects1.entrySet()) {
            String projectId = entry.getKey();
            LocalDate[] dateRange1 = entry.getValue();

            if (projects2.containsKey(projectId)) {
                LocalDate[] dateRange2 = projects2.get(projectId);
                duration += getDurationOverlap(dateRange1, dateRange2);
            }
        }

        return duration;
    }
}
