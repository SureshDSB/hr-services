package com.nphc.hr.model;

import com.nphc.hr.util.LocalDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "login")
    private String login;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "salary")
    private BigDecimal salary;

    @CsvCustomBindByName(column = "startDate", converter = LocalDateConverter.class)
    private LocalDate startDate;


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof EmployeeDto employeeDto)) {
            return false;
        }
        if (employeeDto.id.equals(this.id)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
