package com.nphc.hr.util;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.LocalDate;

import static com.nphc.hr.util.Constant.DATE_TIME_FORMATTER;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return LocalDate.parse(s, DATE_TIME_FORMATTER);
    }
}
