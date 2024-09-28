package com.fyl.demo.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyl.demo.AppConstant.MessageConfig;
import com.fyl.demo.AppConstant.ResponseHandler;
import com.fyl.demo.dto.EmpInfoRequestDTO;
import com.fyl.demo.model.EmployeeDepartment;
import com.fyl.demo.model.EmployeeDesignation;
import com.fyl.demo.model.EmployeeInformation;
import com.fyl.demo.repository.EmployeeDepartmentRepository;
import com.fyl.demo.repository.EmployeeDesignationRepository;
import com.fyl.demo.repository.EmployeeInformationRepository;
import com.fyl.demo.service.EmployeeInformationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeInformationController {

    @Autowired
    private EmployeeInformationService employeeService;

    @Autowired
    private EmployeeInformationRepository employeeRepository;

    @Autowired
    private EmployeeDesignationRepository designationRepository;

    @Autowired
    private EmployeeDepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

// check only validateEmpAddRequest , new data insert working
    @PostMapping("/addEmployeeWithImage")
    public ResponseEntity<Object> addEmployeeWithImage(@RequestParam("empInfo") String empInfo, @RequestParam MultipartFile image) throws IOException {
        EmpInfoRequestDTO empInfoRequestDTO = objectMapper.readValue(empInfo, EmpInfoRequestDTO.class);
        List<String> error = validateEmpAddRequest(empInfoRequestDTO);
        String imageError = validateImage(image);
//        Optional<EmployeeDepartment> departmentOptional = Optional.empty();
//        Optional<EmployeeDesignation> designationOptional = Optional.empty();

        if (error.isEmpty() != true || imageError !=null) {
//            EmployeeDepartment department = departmentRepository.findById(empInfoRequestDTO.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department Not Found with "+empInfoRequestDTO.getDepartmentId()));
//            EmployeeDesignation designation = designationRepository.findById(empInfoRequestDTO.getDesignationId()).orElseThrow(() -> new RuntimeException("Designation Not Found with "+empInfoRequestDTO.getDesignationId()));
//              departmentOptional = Optional.of(department);
//              designationOptional = Optional.of(designation);
             error.add(imageError);
            return ResponseHandler.generateResponse(null, HttpStatus.BAD_REQUEST, error.toString());

        }
        EmployeeDepartment department = departmentRepository.findById(empInfoRequestDTO.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department Not Found with "+empInfoRequestDTO.getDepartmentId()));
        EmployeeDesignation designation = designationRepository.findById(empInfoRequestDTO.getDesignationId()).orElseThrow(() -> new RuntimeException("Designation Not Found with "+empInfoRequestDTO.getDesignationId()));
//        departmentOptional = Optional.of(department);
//        designationOptional = Optional.of(designation);


           Object returnedObj =  employeeService.addEmployeeWithImage(empInfoRequestDTO, image,department,designation);
        return ResponseHandler.generateResponse(MessageConfig.SUCCESS_ADD_EMPLOYEE_IMAGE_SUCCESSFULLY,HttpStatus.CREATED,returnedObj);
    }
  //validating image
    private String validateImage(MultipartFile image) {
        if (image.isEmpty()) {
            return MessageConfig.ERROR_IMAGE_IS_EMPTY;
        }
        return null;
    }

    //working
    @PostMapping("/addEmployee")
    public ResponseEntity<Object> addEmployee(@RequestBody EmpInfoRequestDTO empInfoRequestDTO) throws MessagingException {

        List error = validateEmpAddRequest(empInfoRequestDTO);
        if (error.isEmpty() == true) {
            EmployeeDepartment department = departmentRepository.findById(empInfoRequestDTO.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department Not Found with "+empInfoRequestDTO.getDepartmentId()));
            EmployeeDesignation designation = designationRepository.findById(empInfoRequestDTO.getDesignationId()).orElseThrow(() -> new RuntimeException("Designation Not Found with "+empInfoRequestDTO.getDesignationId()));
          EmployeeInformation employeeInformation =  employeeService.addEmployee(empInfoRequestDTO, department, designation);
            return ResponseHandler.generateResponse(MessageConfig.SUCCESS_EMPLOYEE_ADDED_SUCCESSFULLY, HttpStatus.CREATED, employeeInformation);

        }

        return ResponseHandler.generateResponse(error.toString(), HttpStatus.BAD_REQUEST, "Not Generated");


    }

//working
    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable("id") long employeeId,@RequestParam("empInfo") String empInfo, @RequestParam MultipartFile image) throws Exception{
        EmpInfoRequestDTO empInfoRequestDTO = objectMapper.readValue(empInfo, EmpInfoRequestDTO.class);
        System.out.println(empInfoRequestDTO);
      Object updateEmployee = employeeService.updateEmployee(empInfoRequestDTO,employeeId,image);
            return ResponseHandler.generateResponse("No Error in Update", HttpStatus.OK, updateEmployee);
        }


    //working
    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") long employeeId) {
        Optional<EmployeeInformation> employeeDelete = employeeRepository.findById(employeeId);
        if (employeeDelete.isEmpty()) {
            return ResponseHandler.generateResponse(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }
//        unlinkDesignationAndDepartmentFromEmployee(employeeDelete.get());
        Object responseEmpfromDb = employeeService.deleteEmployee(employeeId);
        if (responseEmpfromDb == null) {
            return ResponseHandler.generateResponse(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.NO_CONTENT, null);
        } else {
            return ResponseHandler.generateResponse(MessageConfig.SUCCESS_EMPLOYEE_DELETED_SUCCESSFULLY, HttpStatus.NO_CONTENT, responseEmpfromDb);
        }

    }
//
//    // get All Employees
//    @GetMapping("/getAllEmployees")
//    public ResponseEntity<Object> getAllEmployees() {
//        return ResponseHandler.generateResponse(MessageConfig.SUCCESS_EMPLOYEE_FETCHING,HttpStatus.OK,null);
//    }

    //working
    @GetMapping("/getAllEmployees")
    public ResponseEntity<Object> getAllEmployees() {
        List<EmployeeInformation> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return ResponseHandler.generateResponse(MessageConfig.ERROR_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }
        return ResponseHandler.generateResponse(MessageConfig.SUCCESS_EMPLOYEE_FETCHING, HttpStatus.OK, employees);
    }
//working
    private List<String> validateEmpAddRequest(EmpInfoRequestDTO employee) {
        Optional<EmployeeDesignation> designationOptional = designationRepository.findById(employee.getDesignationId());
        Optional<EmployeeDepartment> departmentOptional = departmentRepository.findById(employee.getDepartmentId());

        List<String> errors = new ArrayList<>();
        if (employee.getName() == null || employee.getName().isEmpty()) {
            errors.add(MessageConfig.ERROR_EMPLOYEE_NAME_REQUIRED);

        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            errors.add(MessageConfig.ERROR_EMPLOYEE_EMAIL_REQUIRED);


        }
        //added
        if(employeeService.validateEmail(employee.getEmail())){
            errors.add(MessageConfig.ERROR_EMPLOYEE_EMAIL_ALREADY_EXISTS);

        }
        if (designationOptional.isEmpty()) {
            errors.add(MessageConfig.ERROR_DESIGNATION_NOT_FOUND);
//            return ResponseHandler.generateResponse(MessageConfig.ERROR_DESIGNATION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }
        if (departmentOptional.isEmpty()) {
            errors.add(MessageConfig.ERROR_DEPARTMENT_NOT_FOUND);
//            return ResponseHandler.generateResponse(MessageConfig.ERROR_DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }





        return errors;
    }

    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable("id") long employeeId) {
        Optional<EmployeeInformation> employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            return ResponseHandler.generateResponse("Employee Doesnot Exist",
                    HttpStatus.NOT_FOUND,MessageConfig.ERROR_EMPLOYEE_NOT_FOUND);
        }
        return ResponseHandler.generateResponse("Employee Found",HttpStatus.OK,employee);
    }

    @PostMapping("/employeeDeletionValid/{id}")
    public ResponseEntity<Object> employeeDeletionValid(@PathVariable("id") long employeeId) {
        return employeeService.makeEmployeeActive(employeeId);
    }






}
