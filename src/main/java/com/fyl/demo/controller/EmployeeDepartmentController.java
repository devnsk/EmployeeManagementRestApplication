package com.fyl.demo.controller;
import com.fyl.demo.AppConstant.MessageConfig;
import com.fyl.demo.AppConstant.ResponseHandler;
import com.fyl.demo.dto.DepartmentRequestDTO;
import com.fyl.demo.model.EmployeeDepartment;
import com.fyl.demo.repository.EmployeeDepartmentRepository;
import com.fyl.demo.service.EmployeeDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/department")
public class EmployeeDepartmentController {

    @Autowired
    private EmployeeDepartmentService departmentService;
    @Autowired
    private EmployeeDepartmentRepository employeeDepartmentRepository;

    @PostMapping("/addDepartment")
    public ResponseEntity<String> addDepartment(@RequestBody EmployeeDepartment department) {
        departmentService.saveDepartment(department);
        return ResponseEntity.ok(MessageConfig.SUCCESS_DEPARTMENT_ADDED_SUCCESSFULLY);
    }

    @GetMapping("/getDepartmentById/{id}")
    public ResponseEntity<EmployeeDepartment> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping("/getAllDepartments")
    public ResponseEntity<List<EmployeeDepartment>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        if(employeeDepartmentRepository.findById(id).isEmpty()) {
            return ResponseEntity.ok("Department not found with ID: " + id);
        }
        EmployeeDepartment department = departmentService.getDepartmentById(id);
        StringBuilder res = new StringBuilder();
        if(department.getTotalEmployee() > 0) {
            return ResponseEntity.ok("Unable to Delete Department due to "+ department.getTotalEmployee() + " employee exist under "+department.getDepartmentName());
        }
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(department.getDepartmentName() + " Department Deleted Successfully");
    }

    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<Object> updateDepartment(@PathVariable("id") long departmentId,@RequestBody DepartmentRequestDTO departmentRequestDTO) {
        System.out.println(departmentId);
        Optional<EmployeeDepartment> checkEmpIsPresent = employeeDepartmentRepository.findById(departmentId);
        if (checkEmpIsPresent.isEmpty()) {
            return ResponseHandler.generateResponse(MessageConfig.ERROR_DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        }

       return ResponseEntity.ok(departmentService.updateDepartment(departmentRequestDTO, departmentId));

    }
}
