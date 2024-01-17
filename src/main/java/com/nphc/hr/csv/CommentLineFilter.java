package com.nphc.hr.csv;

import com.opencsv.bean.CsvToBeanFilter;

public class CommentLineFilter implements CsvToBeanFilter{

    @Override
    public boolean allowLine(String[] line) {
        boolean isCommented = line.length !=0 && line[0].startsWith("#");
        return !isCommented;
    }
}
