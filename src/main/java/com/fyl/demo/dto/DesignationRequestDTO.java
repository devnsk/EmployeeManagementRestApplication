package com.fyl.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DesignationRequestDTO {
    private Long designationId;

    private String designationName;
    private long totalEmployees;

//    private Boolean isActive;
//    private String createdBy;
//    private LocalDateTime createdDate;
//    private String updatedBy;
//    private LocalDateTime updatedTime;
}
