package com.nphc.hr.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Constant {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[yyyy-MM-dd]" + "[dd-MMM-yy]")).toFormatter();


    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String UPLOAD_OK = "Successfully uploaded CSV file to the Database";
    public static final String GET_NOT_OK = "No Such Employee";
    public static final String NO_SUCH_EMP = "No Such Employee";
    public static final String SUCCESS_UPDATE_OK = "Successfully updated";
    public static final String EMP_LOGIN_NOT_UNIQUE = "Employee login not unique";
    public static final String EMP_ID_NOT_EQ = "Employee Id not equal";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String DELETED_SUCCESS = "Successfully deleted";
    public static final String UPDATE_SUCCESS = "Successfully updated";
    public static final String SAVE_SUCCESS = "Successfully created";
    public static final String EMP_ID_EXISTS = "EMPLOYEE ID already exists";

}
