package com.fyl.demo.service;

import com.fyl.demo.AppConstant.MessageConfig;
import com.fyl.demo.AppConstant.ResponseHandler;
import com.fyl.demo.dto.EmailDetail;
import com.fyl.demo.dto.EmpInfoRequestDTO;
import com.fyl.demo.model.EmployeeDepartment;
import com.fyl.demo.model.EmployeeDesignation;
import com.fyl.demo.model.EmployeeInformation;
import com.fyl.demo.repository.EmployeeDepartmentRepository;
import com.fyl.demo.repository.EmployeeDesignationRepository;
import com.fyl.demo.repository.EmployeeInformationRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeInformationService {

    @Autowired
    private EmployeeInformationRepository employeeRepository;


    @Autowired
    private EmployeeDesignationRepository designationRepository;

    @Autowired
    private EmployeeDepartmentRepository departmentRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeDepartmentRepository employeeDepartmentRepository;
    @Autowired
    private EmployeeDesignationRepository employeeDesignationRepository;

    public EmployeeInformation addEmployee(EmpInfoRequestDTO empInfoRequestDTO, EmployeeDepartment department, EmployeeDesignation designation) throws MessagingException {
        EmployeeInformation employeeInformation = new EmployeeInformation();
        employeeInformation.setName(empInfoRequestDTO.getName());
        employeeInformation.setEmail(empInfoRequestDTO.getEmail());
        employeeInformation.setPhoneNumber(empInfoRequestDTO.getPhoneNumber());
         employeeInformation.setCreatedBy(empInfoRequestDTO.getCreatedBy());
        employeeInformation.setCreatedDate(LocalDateTime.now());
        employeeInformation.setIsActive(true);
        //here we r counting employee is deprtment and designation
        department.setTotalEmployee(department.getTotalEmployee() + 1);
        designation.setTotalEmployee(designation.getTotalEmployee() + 1);
        // here we r Assing departmetId and designationid to the employee
        employeeInformation.setDepartment(department);
        employeeInformation.setDesignation(designation);
        departmentRepository.save(department);
        designationRepository.save(designation);
        EmployeeInformation emp = employeeRepository.save(employeeInformation);
        if(emp.getIsActive()){
            //prepare emailDto object
            EmailDetail emailDetail =
                    EmailDetail.builder()
                                    .subject("Employee Enrollment")
                                            .content(emp.getEmail() +" has been Successfully connected and activated at @Abc Company.")
                                                    .recipient(emp.getEmail()).build();

            emailService.sendEmail(emailDetail);
        }
        return emp;
    }

    public List<EmployeeInformation> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Object updateEmployee(EmpInfoRequestDTO empInfoRequestDTO, long employeeId, MultipartFile image) throws IOException {
        EmployeeInformation updateEmployee = employeeRepository.findById(employeeId).get();
        if (updateEmployee.getEmployeeId() >0 ){
            if(empInfoRequestDTO.getEmail() != null ){
                updateEmployee.setEmail(empInfoRequestDTO.getEmail());
            }
            if(empInfoRequestDTO.getPhoneNumber() != null ){
                updateEmployee.setPhoneNumber(empInfoRequestDTO.getPhoneNumber());
            }
            if(empInfoRequestDTO.getName() != null ){
                updateEmployee.setName(empInfoRequestDTO.getName());
            }
            if(empInfoRequestDTO.getDepartmentId() != null ){
                updateEmployee.setDepartment(departmentRepository.findById(empInfoRequestDTO.getDepartmentId()).get());
            }
            if(empInfoRequestDTO.getDesignationId() != null ){
                updateEmployee.setDesignation(designationRepository.findById(empInfoRequestDTO.getDesignationId()).get());
            }
            if(image != null){
                updateEmployee.setImage(image.getBytes());
            }

          employeeRepository.save(updateEmployee);

            return updateEmployee;
        }
        return MessageConfig.ERROR_UPDATE_EMPLOYEE;
    }

    public Object deleteEmployee(long employeeId) {
        EmployeeInformation employee = employeeRepository.findById(employeeId).get();
        if(employeeDeletionValid(employee)){
            EmployeeDepartment department = employee.getDepartment();
            System.out.println(department.getTotalEmployee());
            EmployeeDesignation designation = employee.getDesignation();

            //Decrement employee count for department anf designation
            if (department.getTotalEmployee() > 0) {
                department.setTotalEmployee(department.getTotalEmployee() - 1);
                designation.setTotalEmployee(designation.getTotalEmployee() - 1);
            }
            //save update department and designation
            departmentRepository.save(department);
            designationRepository.save(designation);
            employee.setIsActive(false);
            EmployeeInformation employeeInformation = employeeRepository.save(employee);
            if(!employeeInformation.getIsActive()){
//                EmailDetail emailDetail = Email
            }
            return employeeInformation;
        }else{
            return null;
        }


    }

    public Object addEmployeeWithImage(EmpInfoRequestDTO empInfoRequestDTO, MultipartFile image, EmployeeDepartment department, EmployeeDesignation designation) throws IOException {
        EmployeeInformation employeeInformation = new EmployeeInformation();
        employeeInformation.setName(empInfoRequestDTO.getName());
        employeeInformation.setEmail(empInfoRequestDTO.getEmail());
        employeeInformation.setPhoneNumber(empInfoRequestDTO.getPhoneNumber());
         employeeInformation.setCreatedBy(empInfoRequestDTO.getCreatedBy());
        employeeInformation.setCreatedDate(LocalDateTime.now());
        employeeInformation.setIsActive(true);
        employeeInformation.setCreatedDate(LocalDateTime.now());
//        employeeInformation.setImage(image.getBytes());
        //here counting employee is deprtment and designation
        department.setTotalEmployee(department.getTotalEmployee() + 1);
        designation.setTotalEmployee(designation.getTotalEmployee() + 1);
        // Assign departmetId and designationid to the employee
        employeeInformation.setDepartment(department);
        employeeInformation.setDesignation(designation);
        if (image != null && !image.isEmpty()) {
            byte[] imageLogo = image.getBytes();
            employeeInformation.setImage(imageLogo);
        } else {
            employeeInformation.setImage(new byte[0]);
        }
        employeeInformation.setIsActive(true);
        employeeInformation.setCreatedDate(LocalDateTime.now());

        departmentRepository.save(department);
        designationRepository.save(designation);
        employeeRepository.save(employeeInformation);
        return employeeInformation;
    }






    private String validateEmpAddRequest(EmployeeInformation employee) {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            return MessageConfig.ERROR_EMPLOYEE_NAME_REQUIRED;
        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            return MessageConfig.ERROR_EMPLOYEE_EMAIL_REQUIRED;
        }
//        if (employee.getPhoneNumber() == null || employee.getPhoneNumber().isEmpty()) {
//            return MessageConfig.ERROR_EMPLOYEE_MOBILE_NUMBER_REQUIRED;
//        }
        return null;
    }

    public boolean validateEmail(String email) {
        Optional<EmployeeInformation> employeeInformation =employeeRepository.findByEmail(email);
        if (employeeInformation.isPresent()) {
            return true;
        }
        return false;
    }
    private boolean employeeDeletionValid(EmployeeInformation employeeInformation) {
        if(employeeInformation.getIsActive() == true){
            return true;
        }
        else{
            return false;
        }
    }

    public ResponseEntity<Object> makeEmployeeActive(long employeeId) {
        Optional<EmployeeInformation> employee = employeeRepository.findById(employeeId);
        StringBuilder message = new StringBuilder();
        if(employee.isEmpty()){
            message.append("Employee does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        // employee already active ho to
        if(employee.get().getIsActive()){
            message.append("Already Employee is Active. ");
        }
        //employee deactivate ho to
        if(!employee.get().getIsActive()){
            EmployeeInformation employeeInformation = employee.get();
            employeeInformation.setIsActive(true);
            employeeRepository.save(employeeInformation);
            message.append("Employee has been Activated. ");
        }




        //checking the department
//        EmployeeDepartment employeeDepartment = employeeDepartmentRepository.findById(employee.getDepartment().getDepartmentId()).get();
//        if(employeeDepartment == null){
//            message.append("DepartMent is Inactive for this Employee "+ employee.getName());
//        }
//        //checking the designation id
//        EmployeeDesignation employeeDesignation = employeeDesignationRepository.findById(employee.getDesignation().getDesignationId()).get();
//        if(employeeDesignation == null){
//            message.append("Designation is Inactive for this Employee "+ employee.getName());
//        }



        return ResponseEntity.status(HttpStatus.OK).body(message);


    }
}
