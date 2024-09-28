package com.fyl.demo.service;

import com.fyl.demo.AppConstant.MessageConfig;
import com.fyl.demo.AppConstant.ResponseHandler;
import com.fyl.demo.dto.DepartmentRequestDTO;
import com.fyl.demo.model.EmployeeDepartment;
import com.fyl.demo.repository.EmployeeDepartmentRepository;
import com.fyl.demo.repository.EmployeeDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeDepartmentService {

    @Autowired
    private EmployeeDepartmentRepository departmentRepository;

    public EmployeeDepartment saveDepartment(EmployeeDepartment department) {
        if (department.getDepartmentName() == null || department.getDepartmentName().isEmpty()) {
            throw new IllegalArgumentException(MessageConfig.ERROR_DEPARTMENT_NAME_REQUIRED);
        }

        if (departmentRepository.findByDepartmentName(department.getDepartmentName()).isPresent()) {
            throw new IllegalArgumentException(MessageConfig.ERROR_DEPARTMENT_ALREADY_EXISTS);
        }

        return departmentRepository.save(department);
    }

    public EmployeeDepartment getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageConfig.ERROR_DEPARTMENT_NOT_FOUND));
    }

    public List<EmployeeDepartment> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public Object updateDepartment(DepartmentRequestDTO departmentDto,long departmentId) {
        if (departmentDto.getDepartmentName() == null || departmentDto.getDepartmentName().isEmpty()) {

            return ResponseHandler.generateResponse("Department Name is NUll", HttpStatus.NOT_FOUND,MessageConfig.ERROR_DEPARTMENT_NAME_REQUIRED);
        }
        else{
            EmployeeDepartment employeeDepartment = departmentRepository.findById(departmentId).get();
            employeeDepartment.setDepartmentName(departmentDto.getDepartmentName());
            employeeDepartment.setTotalEmployee(departmentDto.getTotalEmployees());
            EmployeeDepartment employeeDepartment1 = departmentRepository.save(employeeDepartment);
            return ResponseHandler.generateResponse("Department Updated", HttpStatus.OK, employeeDepartment1);
        }
    }
}
