package com.fyl.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
    public class EmployeeDesignation {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long designationId;

        private String designationName;
        private long totalEmployee;

//    private Boolean isActive;
//    private String createdBy;
//    private LocalDateTime createdDate;
//    private String updatedBy;
//    private LocalDateTime updatedTime;

}
