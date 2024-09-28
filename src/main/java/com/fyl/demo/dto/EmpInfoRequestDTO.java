package com.fyl.demo.dto;

import com.fyl.demo.model.EmployeeDepartment;
import com.fyl.demo.model.EmployeeDesignation;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpInfoRequestDTO {
    private Long employeeId;
    private String name;
    private String email;
    private String phoneNumber;
    private Long designationId;
    private Long departmentId;

    private Boolean isActive;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedTime;
}
