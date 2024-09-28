package com.fyl.demo.service;

import com.fyl.demo.AppConstant.MessageConfig;
import com.fyl.demo.model.EmployeeDesignation;
import com.fyl.demo.repository.EmployeeDepartmentRepository;
import com.fyl.demo.repository.EmployeeDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeDesignationService {

    @Autowired
    private EmployeeDesignationRepository designationRepository;

    public EmployeeDesignation saveDesignation(EmployeeDesignation designation) {
        if (designation.getDesignationName() == null || designation.getDesignationName().isEmpty()) {
            throw new IllegalArgumentException(MessageConfig.ERROR_DESIGNATION_NAME_REQUIRED);
        }

        if (designationRepository.findByDesignationName(designation.getDesignationName()).isPresent()) {
            throw new IllegalArgumentException(MessageConfig.ERROR_DESIGNATION_ALREADY_EXISTS);
        }

        return designationRepository.save(designation);
    }

    public EmployeeDesignation getDesignationById(Long id) {
        return designationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageConfig.ERROR_DESIGNATION_NOT_FOUND));
    }

    public List<EmployeeDesignation> getAllDesignations() {
        return designationRepository.findAll();
    }

    public void deleteDesignation(Long id) {
        designationRepository.deleteById(id);
    }
}
