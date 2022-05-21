package com.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateValidatorUsingDateTimeFormatter implements DateValidator {
    private final DateTimeFormatter dateFormatter;

    public DateValidatorUsingDateTimeFormatter(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public boolean isValid(String dateStr) {
        try {
            this.dateFormatter.parse(dateStr);
            // check if Date it's before today
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String todayStr = localDate.format(dateFormatter);

            return isXBeforeY(todayStr, dateStr);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public boolean isStartBeforeEnd(String startDate, String endDate) {
        return isXBeforeY(startDate, endDate);
    }

    private boolean isXBeforeY(String X, String Y) {
        String[] elementsX, elementsY;
        List<String> fixedLengthListX, fixedLengthListY;
        int dayX, dayY, monthX, monthY, yearX, yearY;

        elementsX = X.split("/");
        elementsY = Y.split("/");
        fixedLengthListX = Arrays.asList(elementsX);
        fixedLengthListY = Arrays.asList(elementsY);
        dayX = Integer.parseInt(fixedLengthListX.get(0));
        dayY = Integer.parseInt(fixedLengthListY.get(0));
        monthX = Integer.parseInt(fixedLengthListX.get(1));
        monthY = Integer.parseInt(fixedLengthListY.get(1));
        yearX = Integer.parseInt(fixedLengthListX.get(2));
        yearY = Integer.parseInt(fixedLengthListY.get(2));

        return dayX <= dayY && monthX <= monthY && yearY <= yearX;
    }
}
