package com.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidatorUsingDateTimeFormatter implements DateValidator {

    private final DateTimeFormatter dateFormatter;

    public DateValidatorUsingDateTimeFormatter(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public boolean isValid(String dateStr) {
        try {
            this.dateFormatter.parse(dateStr);
            LocalDate localDate = LocalDate.now();
            LocalDate localDate1 = LocalDate.parse(dateStr, this.dateFormatter);
            return localDate.isBefore(localDate1);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public boolean isStartBeforeEnd(String startDate, String endDate) {
        LocalDate localDate1 = LocalDate.parse(startDate, this.dateFormatter);
        LocalDate localDate2 = LocalDate.parse(endDate, this.dateFormatter);
        return localDate1.isBefore(localDate2);
    }
}
