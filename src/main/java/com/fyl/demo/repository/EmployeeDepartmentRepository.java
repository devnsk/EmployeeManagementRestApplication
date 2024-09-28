package com.fyl.demo.repository;

import com.fyl.demo.model.EmployeeDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDepartmentRepository extends JpaRepository <EmployeeDepartment, Long> {
    Optional<EmployeeDepartment> findByDepartmentName(String departmentName);
}
