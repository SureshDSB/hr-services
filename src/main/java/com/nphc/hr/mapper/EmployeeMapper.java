package com.nphc.hr.mapper;

import com.nphc.hr.dao.Employee;
import com.nphc.hr.model.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface EmployeeMapper {


    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDto mapToEmployeeDto(Employee employee);

    Employee mapToEmployee(EmployeeDto employeeDto);

    List<Employee> mapToEmployees(List<EmployeeDto> employeeDtoList);

    List<EmployeeDto> mapToEmployeesDto(List<Employee> employees);


    Set<Employee> mapToEmployeeSet(Set<EmployeeDto> employeeDtoList);
}
