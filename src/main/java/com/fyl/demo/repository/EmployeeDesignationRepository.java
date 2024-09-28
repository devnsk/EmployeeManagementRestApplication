package com.fyl.demo.repository;

import com.fyl.demo.model.EmployeeDepartment;
import com.fyl.demo.model.EmployeeDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDesignationRepository extends JpaRepository<EmployeeDesignation,Long> {

    Optional<EmployeeDesignation> findByDesignationName(String designationName);

}
