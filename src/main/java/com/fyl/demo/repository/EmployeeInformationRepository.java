package com.fyl.demo.repository;


import com.fyl.demo.model.EmployeeInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeInformationRepository extends JpaRepository<EmployeeInformation,Long> {
    Optional<EmployeeInformation> findByEmail(String email);
}
