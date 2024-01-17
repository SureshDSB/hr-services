package com.nphc.hr.repo;

import com.nphc.hr.dao.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepo extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {


}
