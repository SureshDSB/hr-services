package com.nphc.hr.service;

import com.nphc.hr.model.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IEmployeeService {

    String upload(MultipartFile file);

    EmployeeDto getEmployee(String id);
    List<EmployeeDto> getEmployees(Map<String, String> paramsMap);

    String delete(String id);

    String put(EmployeeDto employeeDto);

    String patch(EmployeeDto employeeDto, String id);

    String save(EmployeeDto employeeDto);

}
