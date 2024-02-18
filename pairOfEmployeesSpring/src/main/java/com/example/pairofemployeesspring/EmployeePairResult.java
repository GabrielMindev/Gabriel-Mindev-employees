package com.example.pairofemployeesspring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeePairResult {
    private String employeeId1;
    private String employeeId2;
    private String projectId;
    private long daysWorked;

}