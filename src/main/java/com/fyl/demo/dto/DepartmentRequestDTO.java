package com.fyl.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentRequestDTO {
    private Long departmentId;

    private String departmentName;
    private long totalEmployees;

//    private Boolean isActive;
//    private String createdBy;
//    private LocalDateTime createdDate;
//    private String updatedBy;
//    private LocalDateTime updatedTime;
}
