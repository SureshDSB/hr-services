package com.nphc.hr.controller;


import com.nphc.hr.model.EmployeeDto;
import com.nphc.hr.model.MessageResponse;
import com.nphc.hr.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.nphc.hr.util.Constant.*;

@Slf4j
@RestController
@RequestMapping("users")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse> fileUpload(@RequestPart(value = "file") MultipartFile file) {
        String message = employeeService.upload(file);
        if (SUCCESS.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(UPLOAD_OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") String id) {
        EmployeeDto employeeDto = employeeService.getEmployee(id);
        if (employeeDto != null) {
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(GET_NOT_OK), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllUsers() {
        try {
            List<EmployeeDto> employees = employeeService.findAll();
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> fetchUsers(@RequestParam Map<String, String> paramsMap) {
        log.info("paramsMap = " + paramsMap);
        try {
            List<EmployeeDto> employees = employeeService.getEmployees(paramsMap);
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@RequestBody EmployeeDto employeeDto) {
        String message = employeeService.save(employeeDto);
        if (SAVE_SUCCESS.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(SAVE_SUCCESS), HttpStatus.CREATED);
        } else if (EMP_LOGIN_NOT_UNIQUE.equals(message) || EMP_ID_EXISTS.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        } else if (INTERNAL_SERVER_ERROR.equals(message)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable("id") String id, @RequestBody EmployeeDto employeeDto) {
        if (!id.equals(employeeDto.getId())) {
            return new ResponseEntity<>(new MessageResponse(EMP_ID_NOT_EQ), HttpStatus.BAD_REQUEST);
        }
        String message = employeeService.put(employeeDto);
        if (SUCCESS_UPDATE_OK.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
        } else if (EMP_LOGIN_NOT_UNIQUE.equals(message) || NO_SUCH_EMP.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        } else if (INTERNAL_SERVER_ERROR.equals(message)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> updatePartialUser(@PathVariable("id") String id, @RequestBody EmployeeDto employeeDto) {
        String message = employeeService.patch(employeeDto, id);
        if (UPDATE_SUCCESS.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
        } else if (EMP_LOGIN_NOT_UNIQUE.equals(message) || NO_SUCH_EMP.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        } else if (INTERNAL_SERVER_ERROR.equals(message)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("id") String id) {
        String message = employeeService.delete(id);
        if (DELETED_SUCCESS.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(DELETED_SUCCESS), HttpStatus.OK);
        } else if (NO_SUCH_EMP.equals(message)) {
            return new ResponseEntity<>(new MessageResponse(NO_SUCH_EMP), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
