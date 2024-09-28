package com.fyl.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String name;
    private String email;
    private String phoneNumber;
    @Lob
    @Column(name = "image")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    @JsonIgnore
    private EmployeeDesignation designation;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private EmployeeDepartment department;

    private Boolean isActive;
    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedTime;


}
