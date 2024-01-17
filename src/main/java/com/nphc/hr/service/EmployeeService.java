package com.nphc.hr.service;

import com.nphc.hr.csv.CommentLineFilter;
import com.nphc.hr.dao.Employee;
import com.nphc.hr.mapper.EmployeeMapper;
import com.nphc.hr.model.EmployeeDto;
import com.nphc.hr.repo.EmployeeRepo;
import com.nphc.hr.spec.EmployeeSpecification;
import com.nphc.hr.spec.SearchCriteria;
import com.nphc.hr.spec.SearchOperation;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.nphc.hr.util.Constant.*;

@Slf4j
@Service
public class EmployeeService implements IEmployeeService {


    @Autowired
    private EmployeeRepo employeeRepo;


    /**
     *
     * @param file
     * @return String
     */
    @Override
    public String upload(MultipartFile file) {
        return parseCsvAndSave(file);

    }

    public List<EmployeeDto> findAll() {
        return EmployeeMapper.MAPPER.mapToEmployeesDto(employeeRepo.findAll());

    }

    private String parseCsvAndSave(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            HeaderColumnNameMappingStrategy<EmployeeDto> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EmployeeDto.class);
            CsvToBean<EmployeeDto> csvToBean = new CsvToBeanBuilder<EmployeeDto>(csvReader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withFilter(new CommentLineFilter())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<EmployeeDto> employees = csvToBean.parse();

            // Check Salary less than zero
            List<EmployeeDto> empLessThanZeroSalary = employees.stream().filter(x->x.getSalary().compareTo(BigDecimal.ZERO) < 0).toList();
            if(!empLessThanZeroSalary.isEmpty()) {
                return lessThanZeroSalaryMessage(empLessThanZeroSalary);
            }


            // finding duplicates from CSV file using Frequency Map
            Map<String, Integer> frequencyMap = new HashMap<>();
            for (EmployeeDto dto : employees) {
                frequencyMap.put(dto.getId(), frequencyMap.getOrDefault(dto.getId(), 0) + 1);
            }

            Map<String, Integer> duplicatesMap = frequencyMap.entrySet().stream()
                    .filter(map -> map.getValue() != 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (duplicatesMap.isEmpty()) {
                List<Employee> employeeList = EmployeeMapper.MAPPER.mapToEmployees(employees);
                employeeRepo.saveAll(employeeList);
                return SUCCESS;
            } else {
                log.info("duplicate employee ids Map = {}", duplicatesMap);
                return duplicateMessage(duplicatesMap);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            return FAIL;
        }

    }

    private String lessThanZeroSalaryMessage(List<EmployeeDto> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("Less than Zero Salary - Employee ids found: ");
        for (EmployeeDto emp : list) {
            sb.append(emp.getId()).append(" with salary ").append(emp.getSalary()).append(" ,");
        }
        return sb.toString();
    }

    private String duplicateMessage(Map<String, Integer> duplicatesMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("Duplicate Employee ids found: ");
        for (Map.Entry<String, Integer> entry : duplicatesMap.entrySet()) {
            sb.append(entry.getKey()).append(" with ").append(entry.getValue()).append(" times,");
        }
        return sb.toString();
    }


    /**
     *
     * @param id
     * @return EmployeeDto
     */
    @Override
    public EmployeeDto getEmployee(String id) {
        Optional<Employee> emp = employeeRepo.findById(id);
        return emp.map(EmployeeMapper.MAPPER::mapToEmployeeDto).orElse(null);
    }


    /**
     *
     * @param paramsMap
     * @return List<Employee>
     */
    @Override
    public List<EmployeeDto> getEmployees(Map<String, String> paramsMap) {

        BigDecimal minSalary = new BigDecimal(paramsMap.getOrDefault("minSalary", "0"));
        BigDecimal maxSalary = new BigDecimal(paramsMap.getOrDefault("maxSalary", "4000"));
        int offSet = Integer.parseInt(paramsMap.getOrDefault("offSet", "0"));
        int limit = Integer.parseInt(paramsMap.getOrDefault("limit", "0"));

        log.info("limit = {}", limit);
        log.info("offSet = {}", offSet);
        log.info("minSalary = {}", minSalary);
        log.info("maxSalary = {}", maxSalary);

        EmployeeSpecification salaryFilter = new EmployeeSpecification();
        salaryFilter.add(new SearchCriteria("salary", minSalary, SearchOperation.GREATER_THAN_EQUAL));
        salaryFilter.add(new SearchCriteria("salary", maxSalary, SearchOperation.LESS_THAN));

        limit = (limit <= 0) ? 1 : limit;
        List<Employee> list = employeeRepo.findAll(salaryFilter, PageRequest.of(offSet, limit)).getContent();
        log.info("Employee list:{}", list);
        return EmployeeMapper.MAPPER.mapToEmployeesDto(list);
    }


    /**
     *
     * @param id
     * @return String
     */
    @Override
    public String delete(String id) {
        Optional<Employee> empDb = employeeRepo.findById(id);
        if (empDb.isPresent()) {
            employeeRepo.deleteById(id);
            return DELETED_SUCCESS;
        } else {
            return NO_SUCH_EMP;
        }

    }

    /**
     *
     * @param employeeDto
     * @return String
     */
    @Override
    public String put(EmployeeDto employeeDto) {
        Employee emp = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        try {
            Optional<Employee> empDb = employeeRepo.findById(emp.getId());
            if (empDb.isPresent()) {
                try {
                    employeeRepo.save(emp);
                    return SUCCESS_UPDATE_OK;
                } catch (DataIntegrityViolationException dive) {
                    return EMP_LOGIN_NOT_UNIQUE;
                }
            } else {
                return NO_SUCH_EMP;
            }
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR;
        }
    }

    /**
     *
     * @param employeeDto
     * @param id
     * @return String
     */
    @Override
    public String patch(EmployeeDto employeeDto, String id) {
        try {
            Optional<Employee> empDb = employeeRepo.findById(id);
            if (empDb.isPresent()) {
                try {
                    Employee emp = convertPartial(empDb.get(), employeeDto);
                    employeeRepo.save(emp);
                    return UPDATE_SUCCESS;
                } catch (DataIntegrityViolationException dive) {
                    return EMP_LOGIN_NOT_UNIQUE;
                }
            } else {
                return NO_SUCH_EMP;
            }
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR;
        }
    }


    @Override
    public String save(EmployeeDto employeeDto) {
        Employee emp = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        try {
            Optional<Employee> empDb = employeeRepo.findById(emp.getId());
            if (empDb.isEmpty()) {
                try {
                    employeeRepo.save(emp);
                    return SAVE_SUCCESS;
                } catch (DataIntegrityViolationException dive) {
                    return EMP_LOGIN_NOT_UNIQUE;
                }
            }
            return EMP_ID_EXISTS;
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR;
        }
    }


    private Employee convertPartial(Employee emp, EmployeeDto employeeDto) {
        if (employeeDto.getId() != null) {
            emp.setId(employeeDto.getId());
        }
        if (employeeDto.getLogin() != null) {
            emp.setLogin(employeeDto.getLogin());
        }
        if (employeeDto.getName() != null) {
            emp.setName(employeeDto.getName());
        }
        if (employeeDto.getSalary() != null) {
            emp.setSalary(employeeDto.getSalary());
        }
        if (employeeDto.getStartDate() != null) {
            emp.setStartDate(employeeDto.getStartDate());
        }
        return emp;
    }


}
