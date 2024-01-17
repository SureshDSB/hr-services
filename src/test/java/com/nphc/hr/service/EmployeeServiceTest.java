package com.nphc.hr.service;

import com.nphc.hr.dao.Employee;
import com.nphc.hr.model.EmployeeDto;
import com.nphc.hr.repo.EmployeeRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepo employeeRepo;

    @Test
    void testUpload() throws IOException {
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("init/user-data.csv");
        MockMultipartFile csvFile = new MockMultipartFile("file", "init/user-data.csv", "text/csv", inputStream);
        assertNotNull(employeeService.upload(csvFile));
    }

    @Test
    void testGetEmployee() {
        String id = "e0001";
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Harry");
        when(employeeRepo.findById(id)).thenReturn(Optional.of(emp));
        assertNotNull(employeeService.getEmployee(id));
    }


    @Test
    void testDelete() {
        String id = "e0001";
        assertNotNull(employeeService.delete(id));
    }

    @Test
    void testPut() {
        String id = "e0001";
        EmployeeDto emp = new EmployeeDto();
        emp.setId(id);
        emp.setName("Harry");
        assertNotNull(employeeService.put(emp));
    }


   @Test
    void testPatch() {
        String id = "e0001";
        EmployeeDto emp = new EmployeeDto();
        emp.setId(id);
        emp.setName("Harry");
        assertNotNull(employeeService.patch(emp, id));
    }


   @Test
    void testSave() {
        String id = "e0001";
        EmployeeDto emp = new EmployeeDto();
        emp.setId(id);
        emp.setName("Harry");
        assertNotNull(employeeService.save(emp));
    }


}