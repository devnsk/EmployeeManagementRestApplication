package com.fyl.demo.controller;

import com.fyl.demo.AppConstant.MessageConfig;
import com.fyl.demo.AppConstant.ResponseHandler;
import com.fyl.demo.model.EmployeeDesignation;
import com.fyl.demo.repository.EmployeeDesignationRepository;
import com.fyl.demo.service.EmployeeDesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/designation")
public class EmployeeDesignationController {

    @Autowired
    private EmployeeDesignationService designationService;

    @Autowired
    private EmployeeDesignationRepository designationRepository;

//    @PostMapping("/addDesignation")
//    public ResponseEntity<String> addDesignation(@RequestBody EmployeeDesignation designation) {
//
//        designationService.saveDesignation(designation);
//        return ResponseEntity.ok(MessageConfig.SUCCESS_DESIGNATION_ADDED_SUCCESSFULLY);
//    }

    @GetMapping("getDesignationById/{id}")
    public ResponseEntity<EmployeeDesignation> getDesignationById(@PathVariable Long id) {
        return ResponseEntity.ok(designationService.getDesignationById(id));
    }

    @GetMapping("/getAllDesignations")
    public ResponseEntity<List<EmployeeDesignation>> getAllDesignations() {
        return ResponseEntity.ok(designationService.getAllDesignations());
    }

    @DeleteMapping("/deleteDesignation/{id}")
    public ResponseEntity<String> deleteDesignation(@PathVariable Long id) {
        //write down same logic as written in EmployeeDepartmentController

        designationService.deleteDesignation(id);
        return ResponseEntity.ok("Designation deleted successfully.");
    }

    @PostMapping("/addDesignation")
    public ResponseEntity<Object> addDesignation(@RequestBody EmployeeDesignation designation) throws Exception {
//if(designation.getDesignationName()=){

        List<EmployeeDesignation> repetName= designationRepository.findAll();
        for (EmployeeDesignation employeeDesignation : repetName) {
            if (designation.getDesignationName().equals(employeeDesignation.getDesignationName())) {
                return ResponseHandler.generateResponse("Designation already Exist", HttpStatus.CONFLICT, null);
            }
        }


        if (designation.getDesignationName() == null || designation.getDesignationName().isEmpty()) {
            return ResponseHandler.generateResponse(MessageConfig.ERROR_DESIGNATION_NAME_REQUIRED, HttpStatus.BAD_REQUEST, null);
        }

        ResponseEntity<Object> addDesignation = designationService.saveDesignation(designation);
        return ResponseHandler.generateResponse(MessageConfig.SUCCESS_DESIGNATION_ADDED_SUCCESSFULLY, HttpStatus.OK, null);
    }
}
